package com.example.bookstoreai.repository;

import com.example.bookstoreai.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // --- CRUD ---

    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.id = :id")
    Optional<Book> findByIdWithAuthor(@Param("id") Long id);

    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.slug = :slug")
    Optional<Book> findBySlugWithAuthor(@Param("slug") String slug);

    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.id IN :ids")
    List<Book> findAllByIdWithAuthor(@Param("ids") List<Long> ids);

    @Query(value = """
           SELECT b FROM Book b JOIN FETCH b.author a
           WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           """,
           countQuery = """
           SELECT COUNT(b) FROM Book b JOIN b.author a
           WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           """)
    Page<Book> searchByTitle(@Param("search") String search, Pageable pageable);

    // --- Catalogo publico ---

    @Query(value = "SELECT b FROM Book b JOIN FETCH b.author WHERE b.stock > 0",
            countQuery = "SELECT COUNT(b) FROM Book b WHERE b.stock > 0")
    Page<Book> findAllAvailable(Pageable pageable);

    @Query(value = """
           SELECT b FROM Book b JOIN FETCH b.author a
           WHERE b.stock > 0
           AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
           """,
           countQuery = """
           SELECT COUNT(b) FROM Book b JOIN b.author a
           WHERE b.stock > 0
           AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
           """)
    Page<Book> searchAvailable(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT b FROM Book b JOIN FETCH b.author WHERE b.stock > 0 AND LOWER(b.genre) = LOWER(:genre)",
            countQuery = "SELECT COUNT(b) FROM Book b WHERE b.stock > 0 AND LOWER(b.genre) = LOWER(:genre)")
    Page<Book> findAvailableByGenre(@Param("genre") String genre, Pageable pageable);

    // --- Inteligencia Artificial ---

    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    List<Book> findAllWithAuthor();

    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE LOWER(b.genre) IN :genres AND b.id NOT IN :excludeIds AND b.stock > 0")
    List<Book> findRecommendationsByGenres(@Param("genres") List<String> genres, @Param("excludeIds") List<Long> excludeIds);

    // --- Mis Libros ---

    @Query(value = """
           SELECT DISTINCT b FROM Book b
           JOIN FETCH b.author
           JOIN SaleItem si ON si.book = b
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           """,
           countQuery = """
           SELECT COUNT(DISTINCT b) FROM Book b
           JOIN SaleItem si ON si.book = b
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           """)
    Page<Book> findPurchasedBooks(@Param("email") String email, Pageable pageable);

    @Query(value = """
           SELECT DISTINCT b FROM Book b
           JOIN FETCH b.author
           JOIN SaleItem si ON si.book = b
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
           """,
           countQuery = """
           SELECT COUNT(DISTINCT b) FROM Book b
           JOIN SaleItem si ON si.book = b
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
           """)
    Page<Book> searchPurchasedBooks(@Param("email") String email, @Param("search") String search, Pageable pageable);

    // --- Validaciones ---

    boolean existsBySlug(String slug);

    boolean existsByTitleAndAuthorId(String title, Long authorId);

    boolean existsByTitleAndAuthorIdAndIdNot(String title, Long authorId, Long id);

    boolean existsByAuthorId(Long authorId);

    // --- Carga masiva ---

    Optional<Book> findByTitle(String title);
}
