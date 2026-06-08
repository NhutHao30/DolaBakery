package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "TINTUC")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinTuc {

    @Id
    @Column(name = "MATINTUC", length = 10)
    private String maTinTuc;

    @Column(name = "HINHANH", length = 255)
    private String hinhAnh;

    @Column(name = "NGAYDANG")
    private LocalDate ngayDang;

    @Column(name = "TIEUDE", length = 200)
    private String tieuDe;

    @Column(name = "MOTA", length = 1000)
    private String moTa;
}