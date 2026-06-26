package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.SaleFilterRequest;
import com.example.bookstoreai.dto.request.SaleRequest;
import com.example.bookstoreai.dto.response.PageResponse;
import com.example.bookstoreai.dto.response.SaleResponse;
import com.example.bookstoreai.service.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Gestion de ventas")
public class SaleController {

    private final ISaleService saleService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Crear venta")
    public ResponseEntity<List<SaleResponse>> createSale(@Valid @RequestBody SaleRequest request) {
        return new ResponseEntity<>(saleService.createSale(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    @Operation(summary = "Obtener venta por ID (solo propia o owner)")
    public ResponseEntity<SaleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    @Operation(summary = "Listar ventas (owner: todas, customer: solo propias)")
    public ResponseEntity<PageResponse<SaleResponse>> findAll(
            SaleFilterRequest filter,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(saleService.findAll(filter, pageable)));
    }
}
