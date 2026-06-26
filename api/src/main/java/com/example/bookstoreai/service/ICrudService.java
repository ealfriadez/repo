package com.example.bookstoreai.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICrudService<T, ID> {

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(String search, Pageable pageable);

    T findById(ID id);

    void delete(ID id);
}
