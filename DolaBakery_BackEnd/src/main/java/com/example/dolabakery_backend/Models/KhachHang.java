package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name ="KHACHHANG")
public class KhachHang {
    @Id
    @Column(name = "MAKH", unique = true)
    @JsonProperty("maKH")
    private String MaKH;

    @Column(name = "HOTEN")
    @JsonProperty("hoTen")
    private String HoTen;

    @Column(name = "NGAYSINH")
    @JsonProperty("ngaySinh")
    private LocalDate NgaySinh;

    @Column(name = "SDT")
    @JsonProperty("sdt")
    private String Sdt;

    @Column(name = "GioiTinh")
    @JsonProperty("gioiTinh")
    private String GioiTinh;

    @Column(name = "DIACHI")
    @JsonProperty("diaChi")
    private String DiaChi;

    @Column(name = "DIEMTICHLUY")
    @JsonProperty("diemTichLuy")
    private int DiemTichLuy;

    @Column(name = "USERNAME", unique = true)
    @JsonProperty("userName")
    private String userName;

    public KhachHang(){}

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public int getDiemTichLuy() {
        return DiemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        DiemTichLuy = diemTichLuy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        userName = username;
    }
}