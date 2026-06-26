package com.example.bookstoreai.mapper;

import com.example.bookstoreai.dto.response.ReviewResponse;
import com.example.bookstoreai.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "userName", expression = "java(review.getUser().getEmail())")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "authorFullName", expression = "java(review.getBook().getAuthor().getFirstName() + \" \" + review.getBook().getAuthor().getLastName())")
    ReviewResponse toResponse(Review review);
}
