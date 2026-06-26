package com.example.bookstoreai.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BookRequest(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 200, message = "El titulo no puede superar los 200 caracteres")
        String title,

        @Size(max = 100, message = "El genero no puede superar los 100 caracteres")
        String genre,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal price,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "El ID del autor es obligatorio")
        Long authorId,

        @Size(max = 2000, message = "La descripcion no puede superar los 2000 caracteres")
        String description,

        @Size(max = 300, message = "El slug no puede superar los 300 caracteres")
        String slug
) {}
