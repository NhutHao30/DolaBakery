package com.example.dolabakery_backend.Models;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGioHangId implements Serializable {
    private String magiohang;
    private String masp;
}
