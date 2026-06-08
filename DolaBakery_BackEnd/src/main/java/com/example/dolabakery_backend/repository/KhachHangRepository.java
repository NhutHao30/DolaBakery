package com.example.dolabakery_backend.repository;

import com.example.dolabakery_backend.Models.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.example.dolabakery_backend.dto.KhachHangInfoDTO;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {

    KhachHang findByUserName(String UserName);
    
    @Query("SELECT COUNT(k) > 0 FROM KhachHang k WHERE k.Sdt = :sdt")
    boolean existsBySdt(@org.springframework.data.repository.query.Param("sdt") String sdt);

    @Query("SELECT new com.example.dolabakery_backend.dto.KhachHangInfoDTO(k.MaKH, k.HoTen, k.Sdt, t.email, " +
           "COUNT(h), SUM(h.tongTien)) " +
           "FROM KhachHang k " +
           "LEFT JOIN TaiKhoan t ON k.userName = t.userName " +
           "LEFT JOIN HoaDonBan h ON k.MaKH = h.maKH " +
           "GROUP BY k.MaKH, k.HoTen, k.Sdt, t.email")
    List<KhachHangInfoDTO> findAllWithOrderStats();
}
