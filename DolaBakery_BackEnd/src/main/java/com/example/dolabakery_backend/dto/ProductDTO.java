package com.example.dolabakery_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String MaSP;
    private String TenSP;
    private String GHICHU;
    private BigDecimal GIABAN;
    private String HINHANH;
    private String LOAISP;
    private Integer SOLUONG;
//    private Boolean active;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}
