package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;

@Entity
@Table(name ="VAITRO")
public class VaiTro {
    @Id
    @Column(name = "MAROLE", unique = true)
    private int MaRole;

    @Column(name = "MOTA")
    private String MoTa;

    public VaiTro(){}

    public VaiTro(int maRole, String moTa) {
        MaRole = maRole;
        MoTa = moTa;
    }

    public int getMaRole() {
        return MaRole;
    }

    public void setMaRole(int maRole) {
        MaRole = maRole;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }
}