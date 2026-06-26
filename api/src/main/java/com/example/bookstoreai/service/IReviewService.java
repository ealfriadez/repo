package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.ReviewRequest;
import com.example.bookstoreai.dto.response.ReviewResponse;

import java.util.List;

public interface IReviewService extends ICrudService<ReviewResponse, Long> {

    ReviewResponse create(ReviewRequest request);

    ReviewResponse update(Long id, ReviewRequest request);

    List<ReviewResponse> findByBookId(Long bookId);
}
