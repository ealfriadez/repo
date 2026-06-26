package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.SaleFilterRequest;
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
import com.example.bookstoreai.service.ISaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SaleServiceImpl implements ISaleService {

    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;
    private final SaleItemRepository saleItemRepository;
    private final SaleMapper saleMapper;
    private final SecurityUtil securityUtil;

    @Override
    public List<SaleResponse> createSale(SaleRequest request) {
        Customer customer = securityUtil.getAuthenticatedCustomer();

        List<Long> bookIds = request.items().stream()
                .map(SaleItemRequest::bookId)
                .toList();
        List<Book> books = bookRepository.findAllByIdWithAuthor(bookIds);
        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        BigDecimal total = BigDecimal.ZERO;
        for (SaleItemRequest itemRequest : request.items()) {
            Book book = bookMap.get(itemRequest.bookId());
            if (book == null) {
                throw new ResourceNotFoundException("Book", itemRequest.bookId());
            }
            if (book.getStock() < itemRequest.quantity()) {
                throw new BusinessException(
                        "Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d"
                                .formatted(book.getTitle(), book.getStock(), itemRequest.quantity()));
            }
            book.setStock(book.getStock() - itemRequest.quantity());
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }

        Sale sale = Sale.builder()
                .customer(customer)
                .total(total)
                .build();
        saleRepository.save(sale);

        for (SaleItemRequest itemRequest : request.items()) {
            Book book = bookMap.get(itemRequest.bookId());
            saleItemRepository.insertItem(sale.getId(), book.getId(), itemRequest.quantity(), book.getPrice());
        }

        Sale savedSale = saleRepository.findByIdWithItems(sale.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale", sale.getId()));

        return List.of(saleMapper.toResponse(savedSale));
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long id) {
        Sale sale = saleRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));

        if (securityUtil.isOwner()) {
            // Owner puede ver cualquier venta
        } else {
            // Customer solo puede ver SUS compras
            Customer customer = securityUtil.getAuthenticatedCustomer();
            if (!sale.getCustomer().getId().equals(customer.getId())) {
                throw new AccessDeniedException("No tienes permisos para ver esta venta");
            }
        }

        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponse> findAll(SaleFilterRequest filter, Pageable pageable) {
        if (securityUtil.isOwner()) {
            return filter.hasDateRange()
                    ? saleRepository.findAllByDateRange(filter.dateFrom(), filter.dateTo(), pageable).map(saleMapper::toResponse)
                    : saleRepository.findAllWithItems(pageable).map(saleMapper::toResponse);
        }

        Customer customer = securityUtil.getAuthenticatedCustomer();
        return filter.hasDateRange()
                ? saleRepository.findByCustomerIdAndDateRange(customer.getId(), filter.dateFrom(), filter.dateTo(), pageable).map(saleMapper::toResponse)
                : saleRepository.findByCustomerId(customer.getId(), pageable).map(saleMapper::toResponse);
    }
}
