package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "HDBAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonBan {

    @Id
    @Column(name = "MAHD", length = 10)
    private String maHD;

    @Column(name = "NGAYLAP")
    private LocalDate ngayLap;

    @Column(name = "MAVANDON", length = 50)
    private String maVanDon;

    @Column(name = "DonViVanChuyen", length = 50)
    private String donViVanChuyen;

    @Column(name = "GHICHU", length = 225)
    private String ghiChu;

    @Column(name = "MAKH", length = 100)
    private String maKH;

    @Column(name = "TONGTIEN", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "TRANGTHAI", length = 50)
    private String trangThai;
}