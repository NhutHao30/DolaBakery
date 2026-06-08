package com.example.dolabakery_backend.controller;
import com.example.dolabakery_backend.Models.ChiTietGioHang;
import com.example.dolabakery_backend.Models.ChiTietGioHangId;
import com.example.dolabakery_backend.Models.GioHang;
import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.Models.Product;
import com.example.dolabakery_backend.dto.CartRequest;
import com.example.dolabakery_backend.repository.ChiTietGioHangRepository;
import com.example.dolabakery_backend.repository.GioHangRepository;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import com.example.dolabakery_backend.repository.ProductRepository;
import com.example.dolabakery_backend.repository.HoaDonBanRepository;
import com.example.dolabakery_backend.repository.ChiTietHDBanRepository;
import com.example.dolabakery_backend.Models.HoaDonBan;
import com.example.dolabakery_backend.Models.ChiTietHDBan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.dolabakery_backend.Models.TaiKhoan;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class GioHangController {
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private HoaDonBanRepository hoaDonBanRepository;
    @Autowired
    private ChiTietHDBanRepository chiTietHDBanRepository;

    private String getMaKhachHang(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk == null) return null;
        KhachHang kh = khachHangRepository.findByUserName(tk.getUserName());
        return (kh != null) ? kh.getMaKH() : null;
    }

    @GetMapping
    public ResponseEntity<?> getCart(HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");
        
        GioHang cart = gioHangRepository.findByMakh(makh);
        if (cart == null) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(chiTietGioHangRepository.findByMagiohang(cart.getMagiohang()));
    }
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        Product product = productRepository.findById(request.getMasp()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().body("{\"error\":\"Product not found\"}");
        
        GioHang cart = gioHangRepository.findByMakh(makh);
        if (cart == null) {
            cart = new GioHang();
            cart.setMagiohang(UUID.randomUUID().toString().substring(0, 10));
            cart.setMakh(makh);
            cart.setNgaytao(LocalDate.now());
            gioHangRepository.save(cart);
        }
        ChiTietGioHang item = chiTietGioHangRepository.findByMagiohangAndMasp(cart.getMagiohang(), request.getMasp());
        if (item != null) {
            item.setSoluong(item.getSoluong() + request.getSoluong());
            chiTietGioHangRepository.save(item);
        } else {
            item = new ChiTietGioHang();
            item.setMagiohang(cart.getMagiohang());
            item.setMasp(request.getMasp());
            item.setSoluong(request.getSoluong());
            item.setDonGia(product.getPrice());
            chiTietGioHangRepository.save(item);
        }
        return ResponseEntity.ok(item);
    }
    @DeleteMapping("/{masp}")
    public ResponseEntity<?> removeFromCart(@PathVariable String masp, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        GioHang cart = gioHangRepository.findByMakh(makh);
        if (cart == null) return ResponseEntity.badRequest().body("{\"error\":\"Cart not found\"}");
        ChiTietGioHang item = chiTietGioHangRepository.findByMagiohangAndMasp(cart.getMagiohang(), masp);
        if (item == null) return ResponseEntity.badRequest().body("{\"error\":\"Not found in cart\"}");
        // Dùng native query để tránh lỗi với cột GENERATED ALWAYS AS
        chiTietGioHangRepository.deleteByMagiohangAndMasp(cart.getMagiohang(), masp);
        return ResponseEntity.ok().body("{\"message\":\"Removed\"}");
    }

    @PutMapping("/{masp}")
    public ResponseEntity<?> updateQuantity(@PathVariable String masp, @RequestBody CartRequest request, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        GioHang cart = gioHangRepository.findByMakh(makh);
        if (cart == null) return ResponseEntity.badRequest().body("{\"error\":\"Cart not found\"}");
        ChiTietGioHang item = chiTietGioHangRepository.findByMagiohangAndMasp(cart.getMagiohang(), masp);
        if (item == null) return ResponseEntity.badRequest().body("Item not found in cart");
        if (request.getSoluong() <= 0) {
            chiTietGioHangRepository.deleteByMagiohangAndMasp(cart.getMagiohang(), masp);
            return ResponseEntity.ok().body("{\"message\":\"Removed\"}");
        }
        // Dùng native query UPDATE để tránh lỗi với cột GENERATED ALWAYS AS
        chiTietGioHangRepository.updateSoluong(cart.getMagiohang(), masp, request.getSoluong());
        return ResponseEntity.ok().body("{\"message\":\"Updated\",\"soluong\":" + request.getSoluong() + "}");
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody(required = false) com.example.dolabakery_backend.dto.HoaDonBanDTO request, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        GioHang cart = gioHangRepository.findByMakh(makh);
        if (cart == null) return ResponseEntity.badRequest().body("{\"error\":\"Cart not found\"}");

        List<ChiTietGioHang> items = chiTietGioHangRepository.findByMagiohang(cart.getMagiohang());
        if (items.isEmpty()) return ResponseEntity.badRequest().body("{\"error\":\"Cart is empty\"}");

        // Kiểm tra số lượng tồn kho trước khi thanh toán
        for (ChiTietGioHang item : items) {
            Product product = productRepository.findById(item.getMasp()).orElse(null);
            if (product == null) {
                return ResponseEntity.badRequest().body("{\"error\":\"Sản phẩm không tồn tại: " + item.getMasp() + "\"}");
            }
            if (product.getSoluong() == null || product.getSoluong() < item.getSoluong()) {
                int tonKho = product.getSoluong() != null ? product.getSoluong() : 0;
                return ResponseEntity.badRequest().body("{\"error\":\"Sản phẩm '" + product.getName() + "' không đủ số lượng. Tồn kho chỉ còn: " + tonKho + "\"}");
            }
        }

        // Tính tổng tiền
        java.math.BigDecimal tongTien = java.math.BigDecimal.ZERO;
        for (ChiTietGioHang item : items) {
            java.math.BigDecimal thanhTien = item.getDonGia().multiply(new java.math.BigDecimal(item.getSoluong()));
            tongTien = tongTien.add(thanhTien);
        }

        // Tạo hóa đơn
        String maHD = "HD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        HoaDonBan hoaDon = new HoaDonBan();
        hoaDon.setMaHD(maHD);
        hoaDon.setMaKH(makh);
        hoaDon.setNgayLap(LocalDate.now());
        hoaDon.setTrangThai("Chờ xử lý");
        hoaDon.setTongTien(tongTien);
        if (request != null) {
            hoaDon.setGhiChu(request.getGhiChu());
            hoaDon.setDonViVanChuyen(request.getDonViVanChuyen());
        }
        hoaDonBanRepository.save(hoaDon);

        // Lưu chi tiết hóa đơn
        for (ChiTietGioHang item : items) {
            ChiTietHDBan ct = new ChiTietHDBan();
            ct.setMaHD(maHD);
            ct.setMaSP(item.getMasp());
            ct.setSoLuong(item.getSoluong());
            ct.setDonGia(item.getDonGia());
            // thanhTien is generated
            chiTietHDBanRepository.save(ct);

            // Giảm số lượng tồn kho của sản phẩm
            Product product = productRepository.findById(item.getMasp()).orElse(null);
            if (product != null && product.getSoluong() != null) {
                int newQty = product.getSoluong() - item.getSoluong();
                product.setSoluong(newQty < 0 ? 0 : newQty);
                productRepository.save(product);
            }
        }

        // Xóa giỏ hàng
        chiTietGioHangRepository.deleteByMagiohang(cart.getMagiohang());

        return ResponseEntity.ok(java.util.Map.of("message", "Thanh toán thành công", "maHD", maHD));
    }
}
