package com.example.bookstoreai.repository;

import com.example.bookstoreai.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // --- CRUD ---

    @Query("SELECT r FROM Review r JOIN FETCH r.book b JOIN FETCH b.author JOIN FETCH r.user WHERE b.id = :bookId")
    List<Review> findByBookId(Long bookId);

    // --- Ingesta de datos (vector store) ---

    @Query("SELECT r FROM Review r JOIN FETCH r.book b JOIN FETCH b.author JOIN FETCH r.user")
    List<Review> findAllWithBookAndAuthor();

    // --- Validaciones ---

    long countByBookIdAndUserId(Long bookId, Long userId);
}
