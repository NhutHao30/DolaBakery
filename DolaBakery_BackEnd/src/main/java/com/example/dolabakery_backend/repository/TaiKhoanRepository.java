package com.example.dolabakery_backend.repository;

import com.example.dolabakery_backend.Models.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

    TaiKhoan findByUserNameAndPassword(
            String UserName,
            String Password
    );

    TaiKhoan findByUserName(String UserName);
    TaiKhoan findByEmail(String email);

}