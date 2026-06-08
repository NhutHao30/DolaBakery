package com.example.dolabakery_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangInfoDTO {
    private String maKH;
    private String hoTen;
    private String sdt;
    private String email;
    private Long totalOrders;
    private BigDecimal totalSpent;
    private String membershipRank;

    public KhachHangInfoDTO(String maKH, String hoTen, String sdt, String email, Long totalOrders, BigDecimal totalSpent) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.totalOrders = totalOrders != null ? totalOrders : 0L;
        this.totalSpent = totalSpent != null ? totalSpent : BigDecimal.ZERO;
        this.membershipRank = calculateRank(this.totalSpent);
    }

    private String calculateRank(BigDecimal totalSpent) {
        if (totalSpent == null) return "Đồng";
        double spent = totalSpent.doubleValue();
        if (spent >= 10_000_000) return "Thách đấu";
        if (spent >= 7_000_000) return "Kim cương";
        if (spent >= 5_000_000) return "Vàng";
        if (spent >= 3_000_000) return "Bạc";
        return "Đồng";
    }
}
