package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name ="NHANVIEN")
public class NhanVien {
    @Id
    @Column(name = "USERNAME", unique = true)
    @JsonProperty("userName")
    private String UserName;

    @Column(name = "HOTEN")
    @JsonProperty("hoTen")
    private String HoTen;

    @Column(name = "NGAYSINH")
    @JsonProperty("ngaySinh")
    private LocalDate NgaySinh;

    @Column(name = "GioiTinh")
    @JsonProperty("gioiTinh")
    private String GioiTinh;

    @Column(name = "DIACHI")
    @JsonProperty("diaChi")
    private String DiaChi;

    @Column(name = "SDT")
    @JsonProperty("sdt")
    private String Sdt;

    @Column(name = "CHUCVU")
    @JsonProperty("chucVu")
    private String ChucVu;

    @Column(name = "LUONG")
    @JsonProperty("luong")
    private BigDecimal Luong;
    
    @Column(name = "CALAMVIEC")
    @JsonProperty("caLamViec")
    private String CaLamViec;
    
    @Column(name = "TRANGTHAI")
    @JsonProperty("trangThai")
    private String TrangThai;

    @Transient
    @JsonProperty("maRole")
    private Integer maRole;

    public NhanVien(){}

    public String getChucVu() {
        return ChucVu;
    }

    public void setChucVu(String chucVu) {
        ChucVu = chucVu;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public BigDecimal getLuong() {
        return Luong;
    }

    public void setLuong(BigDecimal luong) {
        Luong = luong;
    }

    public String getCaLamViec() {
        return CaLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        CaLamViec = caLamViec;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public Integer getMaRole() {
        return maRole;
    }

    public void setMaRole(Integer maRole) {
        this.maRole = maRole;
    }
}