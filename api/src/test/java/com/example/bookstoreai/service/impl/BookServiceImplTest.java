package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.BookRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.BookMapper;
import com.example.bookstoreai.model.Author;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.AuthorRepository;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.util.FileStorageUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    // Solo mockeamos dependencias que acceden a BD o archivos
    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @Mock BookMapper bookMapper;
    @Mock FileStorageUtil fileStorageUtil;

    @InjectMocks
    BookServiceImpl bookService;

    @Test
    void findById_ok() {
        Book book = buildBook(1L, "Clean Code");
        BookResponse response = buildResponse(1L, "Clean Code");

        when(bookRepository.findByIdWithAuthor(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.findById(1L);

        assertThat(result.title()).isEqualTo("Clean Code");
    }

    @Test
    void findById_notFound_throwsException() {
        when(bookRepository.findByIdWithAuthor(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_withSearch_usesSearchQuery() {
        // List.of(...) no se mockea, se crea directamente
        var page = new PageImpl<>(List.of(buildBook(1L, "Clean Code")));
        when(bookRepository.searchByTitle(eq("Clean"), any(Pageable.class))).thenReturn(page);
        when(bookMapper.toResponse(any(Book.class))).thenReturn(buildResponse(1L, "Clean Code"));

        var result = bookService.findAll("Clean", Pageable.unpaged());

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void create_duplicateTitle_throwsException() {
        BookRequest request = buildRequest(1L, "Clean Code");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(buildAuthor(1L)));
        when(bookRepository.existsByTitleAndAuthorId("Clean Code", 1L)).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(request, null, null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ya existe un libro");
    }

    @Test
    void create_authorNotFound_throwsException() {
        BookRequest request = buildRequest(99L, "Clean Code");

        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.create(request, null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ok_savesBook() {
        BookRequest request = buildRequest(1L, "Clean Code");
        Book book = buildBook(1L, "Clean Code");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(buildAuthor(1L)));
        when(bookRepository.existsByTitleAndAuthorId("Clean Code", 1L)).thenReturn(false);
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(any())).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(buildResponse(1L, "Clean Code"));

        BookResponse result = bookService.create(request, null, null);

        assertThat(result.title()).isEqualTo("Clean Code");
        verify(bookRepository).save(any());
    }

    @Test
    void delete_notFound_throwsException() {
        when(bookRepository.findByIdWithAuthor(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_ok() {
        when(bookRepository.findByIdWithAuthor(1L)).thenReturn(Optional.of(buildBook(1L, "Clean Code")));

        bookService.delete(1L);

        verify(bookRepository).deleteById(1L);
    }

    // --- datos de prueba ---

    private Book buildBook(Long id, String title) {
        return Book.builder()
                .id(id).title(title).genre("Tech")
                .price(new BigDecimal("50.00")).stock(10).slug("slug")
                .author(buildAuthor(1L))
                .build();
    }

    private Author buildAuthor(Long id) {
        return Author.builder().id(id).firstName("Robert").lastName("Martin").build();
    }

    private BookRequest buildRequest(Long authorId, String title) {
        return new BookRequest(title, "Tech", new BigDecimal("50.00"), 10, authorId, "Desc", null);
    }

    private BookResponse buildResponse(Long id, String title) {
        return new BookResponse(id, title, "slug", "Tech", new BigDecimal("50.00"), 10, null, "Robert Martin", null, false);
    }
}
