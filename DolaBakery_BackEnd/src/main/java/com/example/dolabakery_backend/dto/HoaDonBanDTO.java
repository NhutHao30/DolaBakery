package com.example.dolabakery_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonBanDTO {
    private String maHD;
    private LocalDate ngayLap;
    private String maVanDon;
    private String donViVanChuyen;
    private String ghiChu;
    private String maKH;
    private BigDecimal tongTien;
    private String trangThai;
}