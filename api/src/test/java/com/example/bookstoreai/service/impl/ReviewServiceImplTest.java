package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.ReviewRequest;
import com.example.bookstoreai.dto.response.ReviewResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.ReviewMapper;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.model.Review;
import com.example.bookstoreai.model.User;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.repository.ReviewRepository;
import com.example.bookstoreai.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock ReviewRepository reviewRepository;
    @Mock BookRepository bookRepository;
    @Mock ReviewMapper reviewMapper;
    @Mock SecurityUtil securityUtil;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Test
    void create_bookNotFound_throwsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.create(new ReviewRequest("Bueno", 5, 99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_maxReviewsReached_throwsException() {
        // un usuario no puede tener más de 3 reseñas por libro
        when(bookRepository.findById(1L)).thenReturn(Optional.of(buildBook(1L)));
        when(securityUtil.getAuthenticatedUser()).thenReturn(buildUser(1L));
        when(reviewRepository.countByBookIdAndUserId(1L, 1L)).thenReturn(3L);

        assertThatThrownBy(() -> reviewService.create(new ReviewRequest("Bueno", 5, 1L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("3 resenas");
    }

    @Test
    void create_ok() {
        Review review = buildReview(1L, buildUser(1L), buildBook(1L));
        ReviewResponse response = new ReviewResponse(1L, "Carlos", "Bueno", 5, "Libro 1", null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(buildBook(1L)));
        when(securityUtil.getAuthenticatedUser()).thenReturn(buildUser(1L));
        when(reviewRepository.countByBookIdAndUserId(1L, 1L)).thenReturn(0L);
        when(reviewRepository.save(any())).thenReturn(review);
        when(reviewMapper.toResponse(review)).thenReturn(response);

        ReviewResponse result = reviewService.create(new ReviewRequest("Bueno", 5, 1L));

        assertThat(result.comment()).isEqualTo("Bueno");
        verify(reviewRepository).save(any());
    }

    @Test
    void delete_notFound_throwsException() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_notOwner_throwsException() {
        // usuario 2 intenta borrar la reseña del usuario 1
        Review review = buildReview(1L, buildUser(1L), buildBook(1L));

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(securityUtil.isOwner()).thenReturn(false);
        when(securityUtil.getAuthenticatedUser()).thenReturn(buildUser(2L));

        assertThatThrownBy(() -> reviewService.delete(1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void delete_ok() {
        Review review = buildReview(1L, buildUser(1L), buildBook(1L));

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(securityUtil.isOwner()).thenReturn(false);
        when(securityUtil.getAuthenticatedUser()).thenReturn(buildUser(1L));

        reviewService.delete(1L);

        verify(reviewRepository).deleteById(1L);
    }

    private User buildUser(Long id) {
        return User.builder().id(id).email("user" + id + "@email.com").build();
    }

    private Book buildBook(Long id) {
        return Book.builder().id(id).title("Libro " + id).build();
    }

    private Review buildReview(Long id, User user, Book book) {
        return Review.builder().id(id).comment("Buen libro").rating(4).user(user).book(book).build();
    }
}
