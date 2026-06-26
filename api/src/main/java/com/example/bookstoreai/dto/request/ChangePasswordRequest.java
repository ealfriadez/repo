package com.example.bookstoreai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
    @NotBlank String currentPassword,
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
             message = "Mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    String newPassword
) {}
