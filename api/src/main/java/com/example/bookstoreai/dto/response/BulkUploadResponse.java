package com.example.bookstoreai.dto.response;

import java.util.List;

public record BulkUploadResponse(
    int created,
    int updated,
    List<BulkError> errors
) {
    public record BulkError(int row, String message) {}
}
