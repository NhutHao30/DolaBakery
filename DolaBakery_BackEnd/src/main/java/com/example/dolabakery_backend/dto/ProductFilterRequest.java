package com.example.dolabakery_backend.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
public class ProductFilterRequest {
    private String keyword;
    private String category;
    private String status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean active;
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String direction = "desc";
}
