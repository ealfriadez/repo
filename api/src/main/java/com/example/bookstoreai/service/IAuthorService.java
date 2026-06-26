package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.AuthorRequest;
import com.example.bookstoreai.dto.response.AuthorResponse;

public interface IAuthorService extends ICrudService<AuthorResponse, Long> {

    AuthorResponse create(AuthorRequest request);

    AuthorResponse update(Long id, AuthorRequest request);
}
