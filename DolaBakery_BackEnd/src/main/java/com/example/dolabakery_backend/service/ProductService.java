package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.*;

public interface ProductService {
    PageResponse<ProductDTO> filterProducts(ProductFilterRequest request);
    ProductDTO getById(String id);
    ProductDTO create(ProductDTO dto);
    ProductDTO update(String id, ProductDTO dto);
    void delete(String id);
}
