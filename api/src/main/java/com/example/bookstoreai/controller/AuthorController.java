package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.AuthorRequest;
import com.example.bookstoreai.dto.response.AuthorResponse;
import com.example.bookstoreai.service.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.bookstoreai.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "CRUD de autores")
public class AuthorController {

    private final IAuthorService authorService;

    @GetMapping
    @Operation(summary = "Listar autores con paginacion")
    public ResponseEntity<PageResponse<AuthorResponse>> findAll(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(authorService.findAll(search, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID")
    public ResponseEntity<AuthorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Crear autor (solo owner)")
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Actualizar autor (solo owner)")
    public ResponseEntity<AuthorResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Eliminar autor (solo owner)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
