package com.example.dolabakery_backend.Models;

import jakarta.persistence.*;

@Entity
@Table(name ="TAIKHOAN")
public class TaiKhoan {

    @Id
    @Column(name = "USERNAME", unique = true)
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "MAROLE")
    private int maRole;

    @Column(name = "EMAIL", unique = true)
    private String email;

    public TaiKhoan(){}

    public TaiKhoan(String userName, String email, int maRole, String password) {
        this.userName = userName;
        this.email = email;
        this.maRole = maRole;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaRole() {
        return maRole;
    }

    public void setMaRole(int maRole) {
        this.maRole = maRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}