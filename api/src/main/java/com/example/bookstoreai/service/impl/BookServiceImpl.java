package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.BookRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.BookMapper;
import com.example.bookstoreai.model.Author;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.AuthorRepository;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.util.FileStorageUtil;
import com.example.bookstoreai.service.IBookService;
import com.example.bookstoreai.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final FileStorageUtil fileStorageUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> findAll(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return bookRepository.searchByTitle(search, pageable)
                    .map(bookMapper::toResponse);
        }
        return bookRepository.findAll(pageable).map(bookMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> findAll(Pageable pageable) {
        return findAll(null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        Book book = bookRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findBySlug(String slug) {
        Book book = bookRepository.findBySlugWithAuthor(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse create(BookRequest request, MultipartFile image, MultipartFile bookFile) {
        Author author = findAuthorById(request.authorId());
        validateUniqueTitle(request.title(), request.authorId(), null);

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        book.setSlug(generateSlug(request.slug(), request.title(), author));

        saveFiles(book, image, bookFile);
        book = bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequest request, MultipartFile image, MultipartFile bookFile) {
        Book book = bookRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        Author author = findAuthorById(request.authorId());
        validateUniqueTitle(request.title(), request.authorId(), id);

        boolean titleChanged = !book.getTitle().equals(request.title());
        bookMapper.updateEntityFromRequest(request, book);
        book.setAuthor(author);

        if (titleChanged) {
            book.setSlug(generateSlug(request.slug(), request.title(), author));
        }

        saveFiles(book, image, bookFile);
        book = bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        fileStorageUtil.deleteBookImage(book.getImageUrl());
        fileStorageUtil.deleteBookFile(book.getFileUrl());
        bookRepository.deleteById(id);
    }

    // --- Helpers privados ---

    private String generateSlug(String slugFromRequest, String title, Author author) {
        if (slugFromRequest != null && !slugFromRequest.isBlank()) {
            return slugFromRequest;
        }
        return SlugUtil.toSlug(title, author.getFirstName(), author.getLastName());
    }

    private void saveFiles(Book book, MultipartFile image, MultipartFile bookFile) {
        if (image != null && !image.isEmpty()) {
            try {
                if (book.getImageUrl() != null) fileStorageUtil.deleteBookImage(book.getImageUrl());
                book.setImageUrl(fileStorageUtil.saveBookImage(image, book.getSlug()));
            } catch (IOException e) {
                throw new BusinessException("Error al guardar la imagen");
            }
        }
        if (bookFile != null && !bookFile.isEmpty()) {
            try {
                if (book.getFileUrl() != null) fileStorageUtil.deleteBookFile(book.getFileUrl());
                book.setFileUrl(fileStorageUtil.saveBookFile(bookFile, book.getSlug()));
            } catch (IOException e) {
                throw new BusinessException("Error al guardar el archivo");
            }
        }
    }

    private void validateUniqueTitle(String title, Long authorId, Long excludeId) {
        boolean exists = excludeId == null
                ? bookRepository.existsByTitleAndAuthorId(title, authorId)
                : bookRepository.existsByTitleAndAuthorIdAndIdNot(title, authorId, excludeId);
        if (exists) {
            throw new BusinessException("Ya existe un libro con este titulo para este autor");
        }
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));
    }
}
