package com.example.bookstoreai.dto.request;

import jakarta.validation.constraints.*;

public record ReviewRequest(

        @NotBlank(message = "El comentario es obligatorio")
        @Size(max = 2000, message = "El comentario no puede superar los 2000 caracteres")
        String comment,

        @NotNull(message = "La calificación es obligatoria")
        @Min(value = 1, message = "La calificación mínima es 1")
        @Max(value = 5, message = "La calificación máxima es 5")
        Integer rating,

        @NotNull(message = "El ID del libro es obligatorio")
        Long bookId
) {}
