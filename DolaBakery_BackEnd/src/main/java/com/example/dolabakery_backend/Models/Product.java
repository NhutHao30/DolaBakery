package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sanpham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "MaSP", length = 20)
    private String MaSP;

    @Column(name = "TENSP", nullable = false, length = 150)
    private String name;

    @Column(name = "GIABAN", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "HINHANH", length = 255)
    private String imageUrl;

    @Column(name = "MALOAI", length = 100)
    private String maloai;

    @Column(name = "GHICHU", length = 1000)
    private String description;

    @Column(name = "SOLUONG")
    private Integer soluong;
}