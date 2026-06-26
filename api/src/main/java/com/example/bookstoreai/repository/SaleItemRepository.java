package com.example.bookstoreai.repository;

import com.example.bookstoreai.model.SaleItem;
import com.example.bookstoreai.model.SaleItemId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, SaleItemId> {

    // --- CRUD ---

    @Modifying
    @Query(value = "INSERT INTO sale_items (sale_id, book_id, quantity, unit_price) VALUES (:saleId, :bookId, :quantity, :unitPrice)", nativeQuery = true)
    void insertItem(@Param("saleId") Long saleId, @Param("bookId") Long bookId, @Param("quantity") Integer quantity, @Param("unitPrice") BigDecimal unitPrice);

    // --- Reportes ---

    @Query("""
           SELECT si.book.title, CONCAT(si.book.author.firstName, ' ', si.book.author.lastName),
           SUM(si.quantity), SUM(si.quantity * si.unitPrice)
           FROM SaleItem si
           WHERE CAST(si.sale.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           GROUP BY si.book.id, si.book.title, si.book.author.firstName, si.book.author.lastName
           ORDER BY SUM(si.quantity) DESC
           """)
    List<Object[]> getTopBooks(@Param("dateFrom") LocalDate dateFrom,
                               @Param("dateTo") LocalDate dateTo,
                               Pageable pageable);

    // --- Inteligencia Artificial ---

    @Query("""
           SELECT DISTINCT si.book.genre FROM SaleItem si
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           """)
    List<String> findPurchasedGenresByUserEmail(@Param("email") String email);

    @Query("""
           SELECT DISTINCT si.book.id FROM SaleItem si
           JOIN si.sale s JOIN s.customer c JOIN c.user u
           WHERE u.email = :email
           """)
    List<Long> findPurchasedBookIdsByUserEmail(@Param("email") String email);

    // --- Validaciones ---

    @Query("""
           SELECT COUNT(si) > 0 FROM SaleItem si
           WHERE si.book.id = :bookId AND si.sale.customer.user.email = :email
           """)
    boolean existsByBookIdAndCustomerEmail(@Param("bookId") Long bookId, @Param("email") String email);
}
