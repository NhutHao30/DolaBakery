package com.example.dolabakery_backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinTucDTO {
    private String maTinTuc;
    private String hinhAnh;
    private LocalDate ngayDang;
    private String tieuDe;
    private String moTa;
}