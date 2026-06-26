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
import com.example.bookstoreai.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;
    private final SecurityUtil securityUtil;

    // -- CRUD --

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> findAll(String search, Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse findById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));
        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewResponse create(ReviewRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", request.bookId()));

        User user = securityUtil.getAuthenticatedUser();
        validateMaxReviewsPerUser(request.bookId(), user.getId());

        Review review = Review.builder()
                .user(user)
                .comment(request.comment())
                .rating(request.rating())
                .book(book)
                .build();

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse update(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));

        if (!securityUtil.isOwner()) {
            User user = securityUtil.getAuthenticatedUser();
            if (!review.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("No tiene permisos para editar esta resena");
            }
        }

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", request.bookId()));

        review.setComment(request.comment());
        review.setRating(request.rating());
        review.setBook(book);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));

        if (!securityUtil.isOwner()) {
            User user = securityUtil.getAuthenticatedUser();
            if (!review.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("No tiene permisos para eliminar esta resena");
            }
        }

        reviewRepository.deleteById(id);
    }

    // -- Logica de negocio --

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> findByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    // -- Validaciones privadas --

    private void validateMaxReviewsPerUser(Long bookId, Long userId) {
        long count = reviewRepository.countByBookIdAndUserId(bookId, userId);
        if (count >= 3) {
            throw new BusinessException("Ya tiene 3 resenas para este libro");
        }
    }
}
