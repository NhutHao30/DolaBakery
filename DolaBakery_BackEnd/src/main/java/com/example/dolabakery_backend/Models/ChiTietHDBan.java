package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CHITIETHDBAN")
@IdClass(ChiTietHDBanId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHDBan {

    @Id
    @Column(name = "MAHD", length = 10)
    private String maHD;

    @Id
    @Column(name = "MASP", length = 10)
    private String maSP;

    @Column(name = "SOLUONG")
    private Integer soLuong;

    @Column(name = "DONGIA", precision = 18, scale = 2)
    private BigDecimal donGia;

    @Column(name = "THANHTIEN", precision = 18, scale = 2, insertable = false, updatable = false)
    private BigDecimal thanhTien;
}
