package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.BookRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.dto.response.BulkUploadResponse;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.repository.SaleItemRepository;
import com.example.bookstoreai.security.SecurityUtil;
import com.example.bookstoreai.mapper.BookMapper;
import com.example.bookstoreai.service.IBookImportService;
import com.example.bookstoreai.util.FileStorageUtil;
import com.example.bookstoreai.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.bookstoreai.dto.response.PageResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "CRUD de libros")
public class BookController {

    private final IBookService bookService;
    private final IBookImportService bookBulkService;
    private final BookRepository bookRepository;
    private final SaleItemRepository saleItemRepository;
    private final SecurityUtil securityUtil;
    private final FileStorageUtil fileStorageService;
    private final BookMapper bookMapper;

    @GetMapping
    @Operation(summary = "Listar libros con paginacion")
    public ResponseEntity<PageResponse<BookResponse>> findAll(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(bookService.findAll(search, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Obtener libro por slug")
    public ResponseEntity<BookResponse> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(bookService.findBySlug(slug));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Crear libro (solo owner)")
    public ResponseEntity<BookResponse> create(
            @Valid @RequestPart("book") BookRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile bookFile) {
        return new ResponseEntity<>(bookService.create(request, image, bookFile), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Actualizar libro (solo owner)")
    public ResponseEntity<BookResponse> update(
            @PathVariable Long id,
            @Valid @RequestPart("book") BookRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile bookFile) {
        return ResponseEntity.ok(bookService.update(id, request, image, bookFile));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Eliminar libro (solo owner)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-books")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Listar libros comprados por el cliente autenticado")
    public ResponseEntity<PageResponse<BookResponse>> getMyBooks(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable pageable) {
        String email = securityUtil.getAuthenticatedEmail();
        Page<Book> books;
        if (search != null && !search.isBlank()) {
            books = bookRepository.searchPurchasedBooks(email, search.trim(), pageable);
        } else {
            books = bookRepository.findPurchasedBooks(email, pageable);
        }
        return ResponseEntity.ok(PageResponse.of(books.map(bookMapper::toResponse)));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Descargar archivo del libro (solo clientes que lo compraron)")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long id) {
        String email = securityUtil.getAuthenticatedEmail();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        if (book.getFileUrl() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este libro no tiene archivo descargable");
        }

        boolean purchased = saleItemRepository.existsByBookIdAndCustomerEmail(id, email);
        if (!purchased) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debes comprar este libro para descargarlo");
        }

        try {
            Path filePath = fileStorageService.getBookFilePath(book.getFileUrl());
            Resource resource = new UrlResource(filePath.toUri());

            String filename = book.getSlug() + book.getFileUrl().substring(book.getFileUrl().lastIndexOf("."));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder al archivo");
        }
    }

    @PostMapping("/bulk-upload")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Carga masiva de libros desde Excel (solo owner)")
    public ResponseEntity<BulkUploadResponse> bulkUpload(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bookBulkService.processExcel(file));
    }
}
