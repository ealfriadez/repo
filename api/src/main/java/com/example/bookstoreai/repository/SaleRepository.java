package com.example.bookstoreai.repository;

import com.example.bookstoreai.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // --- CRUD ---

    @Query("SELECT s FROM Sale s JOIN FETCH s.customer JOIN FETCH s.items i JOIN FETCH i.book WHERE s.id = :id")
    Optional<Sale> findByIdWithItems(@Param("id") Long id);

    @Query(value = """
           SELECT s FROM Sale s JOIN FETCH s.customer JOIN FETCH s.items i JOIN FETCH i.book
           """,
           countQuery = "SELECT COUNT(s) FROM Sale s")
    Page<Sale> findAllWithItems(Pageable pageable);

    @Query(value = """
           SELECT s FROM Sale s JOIN FETCH s.customer JOIN FETCH s.items i JOIN FETCH i.book
           WHERE s.customer.id = :customerId
           """,
           countQuery = "SELECT COUNT(s) FROM Sale s WHERE s.customer.id = :customerId")
    Page<Sale> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @Query(value = """
           SELECT s FROM Sale s JOIN FETCH s.customer JOIN FETCH s.items i JOIN FETCH i.book
           WHERE CAST(s.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           """,
           countQuery = """
           SELECT COUNT(s) FROM Sale s
           WHERE CAST(s.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           """)
    Page<Sale> findAllByDateRange(
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Pageable pageable);

    @Query(value = """
           SELECT s FROM Sale s JOIN FETCH s.customer JOIN FETCH s.items i JOIN FETCH i.book
           WHERE s.customer.id = :customerId
           AND CAST(s.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           """,
           countQuery = """
           SELECT COUNT(s) FROM Sale s
           WHERE s.customer.id = :customerId
           AND CAST(s.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           """)
    Page<Sale> findByCustomerIdAndDateRange(
            @Param("customerId") Long customerId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Pageable pageable);

    // --- Reportes ---

    @Query("""
           SELECT MONTH(s.createdAt), YEAR(s.createdAt), COUNT(s), COALESCE(SUM(s.total), 0)
           FROM Sale s
           WHERE CAST(s.createdAt AS LocalDate) BETWEEN :dateFrom AND :dateTo
           GROUP BY YEAR(s.createdAt), MONTH(s.createdAt)
           ORDER BY YEAR(s.createdAt), MONTH(s.createdAt)
           """)
    List<Object[]> getMonthlySales(@Param("dateFrom") LocalDate dateFrom,
                                   @Param("dateTo") LocalDate dateTo);
}
