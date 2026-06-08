package com.example.dolabakery_backend.Models;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietHDBanId implements Serializable {
    private String maHD;
    private String maSP;

    public ChiTietHDBanId() {}

    public ChiTietHDBanId(String maHD, String maSP) {
        this.maHD = maHD;
        this.maSP = maSP;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHDBanId that = (ChiTietHDBanId) o;
        return Objects.equals(maHD, that.maHD) && Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD, maSP);
    }
}
