package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.BookFilterRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.mapper.BookMapper;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.service.ICatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogServiceImpl implements ICatalogService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Page<BookResponse> searchBooks(BookFilterRequest filter, Pageable pageable) {
        String search = filter.search();
        String genre = filter.genre();

        if (genre != null && !genre.isBlank()) {
            return bookRepository.findAvailableByGenre(genre.trim(), pageable).map(bookMapper::toResponse);
        }

        if (search != null && !search.isBlank()) {
            return bookRepository.searchAvailable(search.trim(), pageable).map(bookMapper::toResponse);
        }

        return bookRepository.findAllAvailable(pageable).map(bookMapper::toResponse);
    }

    @Override
    public Page<BookResponse> findByGenre(String genre, Pageable pageable) {
        return bookRepository.findAvailableByGenre(genre, pageable).map(bookMapper::toResponse);
    }
}
