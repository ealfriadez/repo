package com.example.bookstoreai.mapper;

import com.example.bookstoreai.dto.response.SaleItemResponse;
import com.example.bookstoreai.dto.response.SaleResponse;
import com.example.bookstoreai.model.Sale;
import com.example.bookstoreai.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(sale.getCustomer().getFirstName() + \" \" + sale.getCustomer().getLastName())")
    SaleResponse toResponse(Sale sale);

    @Mapping(target = "bookTitle", expression = "java(item.getBook().getTitle())")
    @Mapping(target = "subtotal", expression = "java(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))")
    SaleItemResponse toResponse(SaleItem item);
}
