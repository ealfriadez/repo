package com.example.bookstoreai.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SaleRequest(

        @NotEmpty(message = "Debe incluir al menos un item")
        @Valid
        List<SaleItemRequest> items
) {}
