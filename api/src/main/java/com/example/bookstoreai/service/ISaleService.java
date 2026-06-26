package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.SaleFilterRequest;
import com.example.bookstoreai.dto.request.SaleRequest;
import com.example.bookstoreai.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISaleService {

    List<SaleResponse> createSale(SaleRequest request);

    SaleResponse findById(Long id);

    Page<SaleResponse> findAll(SaleFilterRequest filter, Pageable pageable);
}
