package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.BookRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IBookService extends ICrudService<BookResponse, Long> {

    BookResponse create(BookRequest request, MultipartFile image, MultipartFile bookFile);

    BookResponse update(Long id, BookRequest request, MultipartFile image, MultipartFile bookFile);

    BookResponse findBySlug(String slug);
}
