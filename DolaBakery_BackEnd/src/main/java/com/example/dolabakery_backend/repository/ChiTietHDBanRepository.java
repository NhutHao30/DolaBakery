package com.example.dolabakery_backend.repository;

import com.example.dolabakery_backend.Models.ChiTietHDBan;
import com.example.dolabakery_backend.Models.ChiTietHDBanId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHDBanRepository extends JpaRepository<ChiTietHDBan, ChiTietHDBanId> {
    List<ChiTietHDBan> findByMaHD(String maHD);
}
