package com.example.bookstoreai.mapper;

import com.example.bookstoreai.dto.request.AuthorRequest;
import com.example.bookstoreai.dto.response.AuthorResponse;
import com.example.bookstoreai.model.Author;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorRequest request);

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(AuthorRequest request, @MappingTarget Author author);
}
