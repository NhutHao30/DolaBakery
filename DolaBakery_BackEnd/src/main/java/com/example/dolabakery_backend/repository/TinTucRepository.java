package com.example.dolabakery_backend.repository;

import com.example.dolabakery_backend.Models.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, String> {
    List<TinTuc> findByTieuDeContainingIgnoreCase(String keyword);
}