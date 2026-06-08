package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.dto.KhachHangInfoDTO;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import com.example.dolabakery_backend.Models.TaiKhoan;
import com.example.dolabakery_backend.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class KhachHangController {
    
    private final KhachHangRepository repo;
    private final TaiKhoanRepository taiKhoanRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<KhachHangInfoDTO> getAll() {
        return repo.findAllWithOrderStats();
    }

    @GetMapping("/{id}")
    public KhachHang getById(@PathVariable String id) {
        return repo.findById(id).orElseThrow();
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody KhachHang kh) {
        if (kh.getSdt() != null && !kh.getSdt().trim().isEmpty()) {
            if (repo.existsBySdt(kh.getSdt().trim())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Số điện thoại này đã được đăng ký!"));
            }
        }

        if(kh.getMaKH() == null || kh.getMaKH().trim().isEmpty()) {
            kh.setMaKH("KH" + java.util.UUID.randomUUID().toString().substring(0,6).toUpperCase());
        }
        
        // Tạo tài khoản tự động bằng SĐT
        if (kh.getSdt() != null && !kh.getSdt().trim().isEmpty()) {
            String sdt = kh.getSdt().trim();
            TaiKhoan existingTk = taiKhoanRepo.findByUserName(sdt);
            if (existingTk == null) {
                TaiKhoan tk = new TaiKhoan();
                tk.setUserName(sdt);
                tk.setPassword(passwordEncoder.encode(sdt)); // Password mặc định là SĐT đã mã hóa
                tk.setMaRole(2); // Mã role = 2 cho khách hàng
                // Nếu FE có gửi email trong model KhachHang (nhưng KhachHang ko có email, nên để null hoặc xử lý riêng)
                taiKhoanRepo.save(tk);
            }
            kh.setUserName(sdt);
        }

        return ResponseEntity.ok(repo.save(kh));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody KhachHang updated) {
        KhachHang kh = repo.findById(id).orElseThrow();
        
        // Nếu số điện thoại thay đổi, kiểm tra xem số mới đã tồn tại chưa
        if (updated.getSdt() != null && !updated.getSdt().trim().equals(kh.getSdt())) {
            if (repo.existsBySdt(updated.getSdt().trim())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Số điện thoại này đã được đăng ký cho khách hàng khác!"));
            }
        }

        kh.setHoTen(updated.getHoTen());
        kh.setSdt(updated.getSdt());
        kh.setNgaySinh(updated.getNgaySinh());
        kh.setGioiTinh(updated.getGioiTinh());
        kh.setDiaChi(updated.getDiaChi());
        // For email update, ideally TaiKhoan should be updated. But keeping simple.
        return ResponseEntity.ok(repo.save(kh));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        KhachHang kh = repo.findById(id).orElse(null);
        if (kh != null) {
            String userName = kh.getUserName();
            // Xóa khách hàng trước để tránh lỗi Foreign Key
            repo.deleteById(id);
            
            // Xóa tài khoản liên kết
            if (userName != null && !userName.trim().isEmpty()) {
                TaiKhoan tk = taiKhoanRepo.findByUserName(userName);
                if (tk != null) {
                    taiKhoanRepo.delete(tk);
                }
            }
        }
    }
}
