package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.ReviewRequest;
import com.example.bookstoreai.dto.response.ReviewResponse;
import com.example.bookstoreai.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "CRUD de resenas de libros")
public class ReviewController {

    private final IReviewService reviewService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener resena por ID")
    public ResponseEntity<ReviewResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Listar resenas por libro")
    public ResponseEntity<List<ReviewResponse>> findByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.findByBookId(bookId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    @Operation(summary = "Crear una resena (user del JWT)")
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    @Operation(summary = "Actualizar una resena (solo propia o owner)")
    public ResponseEntity<ReviewResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    @Operation(summary = "Eliminar una resena (solo propia o owner)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
