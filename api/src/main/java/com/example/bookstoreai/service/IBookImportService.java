package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.response.BulkUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IBookImportService {
    BulkUploadResponse processExcel(MultipartFile file);
}
