package com.example.dolabakery_backend.Models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "CHITIETGIOHANG")
@Data
@NoArgsConstructor
@IdClass(ChiTietGioHangId.class)
public class ChiTietGioHang {

    @Id
    @Column(name = "MAGIOHANG", length = 10)
    private String magiohang;

    @Id
    @Column(name = "MASP", length = 10)
    private String masp;

    @Column(name = "SOLUONG")
    private Integer soluong;

    @Column(name = "DonGia", precision = 18, scale = 2)
    private BigDecimal donGia;

    // ThanhTien là GENERATED ALWAYS AS trong MySQL - chỉ đọc hoàn toàn
    // Dùng columnDefinition để Hibernate không ghi vào đây
    @Column(name = "ThanhTien", insertable = false, updatable = false)
    private BigDecimal thanhTien;

    @Column(name = "GHICHU", length = 255)
    private String ghichu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MASP", referencedColumnName = "MaSP", insertable = false, updatable = false)
    private Product product;
}
