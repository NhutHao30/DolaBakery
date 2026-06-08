package com.example.dolabakery_backend.repository;
import com.example.dolabakery_backend.Models.ChiTietGioHang;
import com.example.dolabakery_backend.Models.ChiTietGioHangId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, ChiTietGioHangId> {
    List<ChiTietGioHang> findByMagiohang(String magiohang);
    ChiTietGioHang findByMagiohangAndMasp(String magiohang, String masp);

    // Dùng native query để tránh Hibernate tự sinh SQL sai với cột GENERATED
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CHITIETGIOHANG WHERE MAGIOHANG = :magiohang AND MASP = :masp", nativeQuery = true)
    void deleteByMagiohangAndMasp(@Param("magiohang") String magiohang, @Param("masp") String masp);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CHITIETGIOHANG SET SOLUONG = :soluong WHERE MAGIOHANG = :magiohang AND MASP = :masp", nativeQuery = true)
    void updateSoluong(@Param("magiohang") String magiohang, @Param("masp") String masp, @Param("soluong") int soluong);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CHITIETGIOHANG WHERE MAGIOHANG = :magiohang", nativeQuery = true)
    void deleteByMagiohang(@Param("magiohang") String magiohang);
}
