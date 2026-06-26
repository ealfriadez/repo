package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.AuthorRequest;
import com.example.bookstoreai.dto.response.AuthorResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.AuthorMapper;
import com.example.bookstoreai.model.Author;
import com.example.bookstoreai.repository.AuthorRepository;
import com.example.bookstoreai.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock AuthorRepository authorRepository;
    @Mock BookRepository bookRepository;
    @Mock AuthorMapper authorMapper;

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    void findById_ok() {
        Author author = buildAuthor(1L);
        AuthorResponse response = new AuthorResponse(1L, "Gabriel", "Garcia Marquez", "Colombiana");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.findById(1L);

        assertThat(result.firstName()).isEqualTo("Gabriel");
    }

    @Test
    void findById_notFound_throwsException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_duplicate_throwsException() {
        AuthorRequest request = new AuthorRequest("Gabriel", "Garcia Marquez", "Colombiana");
        when(authorRepository.existsByFirstNameAndLastName("Gabriel", "Garcia Marquez")).thenReturn(true);

        assertThatThrownBy(() -> authorService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Gabriel Garcia Marquez");
    }

    @Test
    void create_ok() {
        AuthorRequest request = new AuthorRequest("Gabriel", "Garcia Marquez", "Colombiana");
        Author author = buildAuthor(1L);
        AuthorResponse response = new AuthorResponse(1L, "Gabriel", "Garcia Marquez", "Colombiana");

        when(authorRepository.existsByFirstNameAndLastName("Gabriel", "Garcia Marquez")).thenReturn(false);
        when(authorMapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.create(request);

        assertThat(result.firstName()).isEqualTo("Gabriel");
        verify(authorRepository).save(author);
    }

    @Test
    void delete_notFound_throwsException() {
        when(authorRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> authorService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_withBooks_throwsException() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByAuthorId(1L)).thenReturn(true);

        assertThatThrownBy(() -> authorService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("tiene libros asociados");
    }

    @Test
    void delete_ok() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByAuthorId(1L)).thenReturn(false);

        authorService.delete(1L);

        verify(authorRepository).deleteById(1L);
    }

    private Author buildAuthor(Long id) {
        return Author.builder().id(id).firstName("Gabriel").lastName("Garcia Marquez").build();
    }
}
