package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.SaleItemRequest;
import com.example.bookstoreai.dto.request.SaleRequest;
import com.example.bookstoreai.dto.response.SaleResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.SaleMapper;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.model.Customer;
import com.example.bookstoreai.model.Sale;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.repository.SaleItemRepository;
import com.example.bookstoreai.repository.SaleRepository;
import com.example.bookstoreai.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock SaleRepository saleRepository;
    @Mock BookRepository bookRepository;
    @Mock SaleItemRepository saleItemRepository;
    @Mock SaleMapper saleMapper;
    @Mock SecurityUtil securityUtil;

    @InjectMocks
    SaleServiceImpl saleService;

    @Test
    void createSale_bookNotFound_throwsException() {
        SaleRequest request = new SaleRequest(List.of(new SaleItemRequest(99L, 1)));

        when(securityUtil.getAuthenticatedCustomer()).thenReturn(buildCustomer());
        when(bookRepository.findAllByIdWithAuthor(List.of(99L))).thenReturn(List.of());

        assertThatThrownBy(() -> saleService.createSale(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createSale_insufficientStock_throwsException() {
        // libro con solo 2 en stock, pero pide 5
        Book book = buildBook(1L, 2);
        SaleRequest request = new SaleRequest(List.of(new SaleItemRequest(1L, 5)));

        when(securityUtil.getAuthenticatedCustomer()).thenReturn(buildCustomer());
        when(bookRepository.findAllByIdWithAuthor(List.of(1L))).thenReturn(List.of(book));

        assertThatThrownBy(() -> saleService.createSale(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void createSale_ok_savesAndReducesStock() {
        Book book = buildBook(1L, 10);
        Sale sale = Sale.builder().id(1L).customer(buildCustomer()).total(new BigDecimal("100.00")).build();
        SaleResponse response = new SaleResponse(1L, 1L, "Carlos Lopez", new BigDecimal("100.00"), null, List.of());
        SaleRequest request = new SaleRequest(List.of(new SaleItemRequest(1L, 2)));

        when(securityUtil.getAuthenticatedCustomer()).thenReturn(buildCustomer());
        when(bookRepository.findAllByIdWithAuthor(List.of(1L))).thenReturn(List.of(book));
        when(saleRepository.save(any())).thenReturn(sale);
        when(saleRepository.findByIdWithItems(any())).thenReturn(Optional.of(sale));
        when(saleMapper.toResponse(sale)).thenReturn(response);

        saleService.createSale(request);

        verify(saleRepository).save(any());
        verify(saleItemRepository).insertItem(any(), eq(1L), eq(2), any(BigDecimal.class));
    }

    private Book buildBook(Long id, int stock) {
        return Book.builder()
                .id(id).title("Libro " + id)
                .price(new BigDecimal("50.00")).stock(stock)
                .build();
    }

    private Customer buildCustomer() {
        return Customer.builder().id(1L).firstName("Carlos").lastName("Lopez").build();
    }
}
