package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.response.IngestionResponse;
import com.example.bookstoreai.service.IIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingestion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class IngestionController {

    private final IIngestionService ingestionService;

    @PostMapping("/books")
    public ResponseEntity<IngestionResponse> ingestBooks() {
        int total = ingestionService.ingestBooks();
        return ResponseEntity.ok(new IngestionResponse(true, "Ingesta completada", total));
    }
}
