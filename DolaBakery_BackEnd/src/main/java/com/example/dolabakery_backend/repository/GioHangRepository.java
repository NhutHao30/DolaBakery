package com.example.dolabakery_backend.repository;
import com.example.dolabakery_backend.Models.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GioHangRepository extends JpaRepository<GioHang, String> {
    GioHang findByMakh(String makh);
}
