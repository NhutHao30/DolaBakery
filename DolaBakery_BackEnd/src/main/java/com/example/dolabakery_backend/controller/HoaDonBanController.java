package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.dto.*;
import com.example.dolabakery_backend.repository.HoaDonBanRepository;
import com.example.dolabakery_backend.service.HoaDonBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dolabakery_backend.repository.ChiTietHDBanRepository;
import com.example.dolabakery_backend.repository.ProductRepository;
import com.example.dolabakery_backend.Models.Product;

import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.Models.TaiKhoan;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonBanController {

    private final HoaDonBanService service;
    private final ChiTietHDBanRepository chiTietRepo;
    private final ProductRepository productRepo;
    private final KhachHangRepository khachHangRepo;
    private final HoaDonBanRepository hoaDonBanRepo;

    @GetMapping
    public ResponseEntity<PageResponse<HoaDonBanDTO>> getAll(HoaDonBanFilterRequest req) {
        return ResponseEntity.ok(service.filterHoaDon(req));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");
        KhachHang kh = khachHangRepo.findByUserName(tk.getUserName());
        if (kh == null) return ResponseEntity.status(401).body("{\"error\":\"Không tìm thấy thông tin khách hàng\"}");

        // Dùng filter với maKH
        HoaDonBanFilterRequest req = new HoaDonBanFilterRequest();
        req.setMaKH(kh.getMaKH());
        req.setSize(100); // Lấy tối đa 100 đơn cho khách hàng
        return ResponseEntity.ok(service.filterHoaDon(req));
    }

    @GetMapping("/{maHD}")
    public ResponseEntity<HoaDonBanDTO> getById(@PathVariable String maHD) {
        return ResponseEntity.ok(service.getById(maHD));
    }

    @GetMapping("/{maHD}/chi-tiet")
    public ResponseEntity<List<ChiTietHDBanDTO>> getChiTiet(@PathVariable String maHD) {
        List<ChiTietHDBanDTO> details = chiTietRepo.findByMaHD(maHD).stream().map(ct -> {
            String tenSP = productRepo.findById(ct.getMaSP())
                                      .map(Product::getName)
                                      .orElse("Sản phẩm không tồn tại");
            return new ChiTietHDBanDTO(ct.getMaHD(), ct.getMaSP(), tenSP, ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(details);
    }

    @PostMapping
    public ResponseEntity<HoaDonBanDTO> create(@RequestBody HoaDonBanDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{maHD}")
    public ResponseEntity<HoaDonBanDTO> update(@PathVariable String maHD,
                                               @RequestBody HoaDonBanDTO dto) {
        return ResponseEntity.ok(service.update(maHD, dto));
    }

    @DeleteMapping("/{maHD}")
    public ResponseEntity<String> delete(@PathVariable String maHD) {
        service.delete(maHD);
        return ResponseEntity.ok("Đã xóa: " + maHD);
    }
}