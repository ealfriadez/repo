package com.example.bookstoreai.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SaleItemId implements Serializable {

    private Long saleId;

    private Long bookId;
}
