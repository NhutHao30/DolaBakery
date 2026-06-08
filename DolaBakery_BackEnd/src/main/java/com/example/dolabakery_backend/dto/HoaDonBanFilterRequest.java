package com.example.dolabakery_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HoaDonBanFilterRequest {
    private String maKH;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String donViVanChuyen;
    private BigDecimal tongTienMin;
    private BigDecimal tongTienMax;
    private String trangThai;
    private String search; // To search by maHD or maKH
    private int page = 0;
    private int size = 10;
}