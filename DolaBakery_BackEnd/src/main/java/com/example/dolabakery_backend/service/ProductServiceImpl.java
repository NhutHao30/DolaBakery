package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.*;
import com.example.dolabakery_backend.Models.Product;
import com.example.dolabakery_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository repo;

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getMaSP(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl(),
                p.getMaloai(),
                p.getSoluong() != null ? p.getSoluong() : 0
        );
    }

    private Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setMaSP(dto.getMaSP());
        p.setName(dto.getTenSP());
        p.setDescription(dto.getGHICHU());
        p.setPrice(dto.getGIABAN());
        p.setImageUrl(dto.getHINHANH());
        p.setMaloai(dto.getLOAISP());
        p.setSoluong(dto.getSOLUONG() != null ? dto.getSOLUONG() : 0);
        return p;
    }

    @Override
    public PageResponse<ProductDTO> filterProducts(ProductFilterRequest req) {
        int pageNumber = Math.max(req.getPage(), 0);
        int pageSize = Math.min(Math.max(req.getSize(), 1), MAX_PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, buildSort(req));

        Page<Product> page = repo.filterProducts(
                blankToNull(req.getKeyword()),
                blankToNull(req.getCategory()),
                blankToNull(req.getStatus()),
                req.getMinPrice(),
                req.getMaxPrice(),
                pageable
        );

        return new PageResponse<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize()
        );
    }

    @Override
    public ProductDTO getById(String id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id)));
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        if (dto.getMaSP() == null || dto.getMaSP().trim().isEmpty()) {
            dto.setMaSP("SP" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return toDTO(repo.save(toEntity(dto)));
    }

    @Override
    public ProductDTO update(String id, ProductDTO dto) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id));

        p.setName(dto.getTenSP());
        p.setDescription(dto.getGHICHU());
        p.setPrice(dto.getGIABAN());
        p.setImageUrl(dto.getHINHANH());
        p.setMaloai(dto.getLOAISP());
        p.setSoluong(dto.getSOLUONG() != null ? dto.getSOLUONG() : 0);

        return toDTO(repo.save(p));
    }

    @Override
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm: " + id);
        }
        repo.deleteById(id);
    }

    private Sort buildSort(ProductFilterRequest req) {
        String property = switch (req.getSortBy() == null ? "" : req.getSortBy()) {
            case "name", "price", "category" -> req.getSortBy();
            default -> "MaSP";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(req.getDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, property);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}