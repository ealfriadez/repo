package com.example.bookstoreai.mapper;

import com.example.bookstoreai.dto.request.BookRequest;
import com.example.bookstoreai.dto.response.BookResponse;
import com.example.bookstoreai.model.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "minStock", ignore = true)
    Book toEntity(BookRequest request);

    @Mapping(target = "authorFullName", expression = "java(book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName())")
    @Mapping(target = "hasFile", expression = "java(book.getFileUrl() != null && !book.getFileUrl().isBlank())")
    BookResponse toResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "minStock", ignore = true)
    void updateEntityFromRequest(BookRequest request, @MappingTarget Book book);
}
