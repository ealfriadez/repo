package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.BookFilterRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICatalogService {
    Page<BookResponse> searchBooks(BookFilterRequest filter, Pageable pageable);
    Page<BookResponse> findByGenre(String genre, Pageable pageable);
}
