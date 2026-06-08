package com.example.dolabakery_backend.Models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Entity
@Table(name = "GIOHANG")
@Data
@NoArgsConstructor
public class GioHang {
    @Id
    @Column(name = "MAGIOHANG", length = 10)
    private String magiohang;
    @Column(name = "MAKH", length = 100, nullable = false)
    private String makh;
    @Column(name = "NGAYTAO")
    private LocalDate ngaytao;
}
