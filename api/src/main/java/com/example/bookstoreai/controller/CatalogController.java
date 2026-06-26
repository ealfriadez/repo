package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.BookFilterRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.dto.response.PageResponse;
import com.example.bookstoreai.service.ICatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final ICatalogService catalogService;

    @GetMapping("/books")
    public ResponseEntity<PageResponse<BookResponse>> searchBooks(
            BookFilterRequest filter,
            @PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(catalogService.searchBooks(filter, pageable)));
    }

    @GetMapping("/books/genre/{genre}")
    public ResponseEntity<PageResponse<BookResponse>> findByGenre(
            @PathVariable String genre,
            @PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(catalogService.findByGenre(genre, pageable)));
    }
}
