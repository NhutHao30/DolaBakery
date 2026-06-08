package com.example.dolabakery_backend.repository;

import com.example.dolabakery_backend.Models.HoaDonBan;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface HoaDonBanRepository extends JpaRepository<HoaDonBan, String> {

    @Query("""
        SELECT h FROM HoaDonBan h
        WHERE (:maKH IS NULL OR h.maKH = :maKH)
          AND (:trangThai IS NULL OR h.trangThai = :trangThai)
          AND (:search IS NULL OR LOWER(h.maHD) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(h.maKH) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:tuNgay IS NULL OR h.ngayLap >= :tuNgay)
          AND (:denNgay IS NULL OR h.ngayLap <= :denNgay)
          AND (:donViVanChuyen IS NULL OR h.donViVanChuyen = :donViVanChuyen)
          AND (:tongTienMin IS NULL OR h.tongTien >= :tongTienMin)
          AND (:tongTienMax IS NULL OR h.tongTien <= :tongTienMax)
        ORDER BY h.ngayLap DESC
    """)
    Page<HoaDonBan> filterHoaDon(
            @Param("maKH") String maKH,
            @Param("trangThai") String trangThai,
            @Param("search") String search,
            @Param("tuNgay") LocalDate tuNgay,
            @Param("denNgay") LocalDate denNgay,
            @Param("donViVanChuyen") String donViVanChuyen,
            @Param("tongTienMin") BigDecimal tongTienMin,
            @Param("tongTienMax") BigDecimal tongTienMax,
            Pageable pageable
    );
}