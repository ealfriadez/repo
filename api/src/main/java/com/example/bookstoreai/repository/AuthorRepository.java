package com.example.bookstoreai.repository;

import com.example.bookstoreai.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // --- CRUD ---

    @Query(value = """
           SELECT a FROM Author a
           WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           """,
           countQuery = """
           SELECT COUNT(a) FROM Author a
           WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           """)
    Page<Author> searchByName(@Param("search") String search, Pageable pageable);

    // --- Validaciones ---

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastNameAndIdNot(String firstName, String lastName, Long id);

    // --- Carga masiva ---

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
