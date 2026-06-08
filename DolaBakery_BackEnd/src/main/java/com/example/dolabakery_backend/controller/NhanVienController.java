package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.Models.NhanVien;
import com.example.dolabakery_backend.Models.TaiKhoan;
import com.example.dolabakery_backend.repository.NhanVienRepository;
import com.example.dolabakery_backend.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class NhanVienController {

    private final NhanVienRepository repo;
    private final TaiKhoanRepository taiKhoanRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<NhanVien> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public NhanVien getById(@PathVariable String id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NhanVien nv) {
        if (nv.getUserName() == null || nv.getUserName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên đăng nhập (Username) không được để trống!"));
        }

        String username = nv.getUserName().trim();
        if (repo.existsById(username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên đăng nhập (Username) đã tồn tại!"));
        }

        // Tạo tài khoản cho nhân viên
        TaiKhoan existingTk = taiKhoanRepo.findByUserName(username);
        if (existingTk == null) {
            TaiKhoan tk = new TaiKhoan();
            tk.setUserName(username);
            // Mặc định mật khẩu giống username
            tk.setPassword(passwordEncoder.encode(username));
            // Sử dụng role được chọn (0 hoặc 1), mặc định là 1 (nhân viên)
            tk.setMaRole(nv.getMaRole() != null ? nv.getMaRole() : 1);
            taiKhoanRepo.save(tk);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Username đã được sử dụng ở tài khoản khác!"));
        }

        // Tính lương tự động cơ bản theo chức vụ nếu lương null hoặc 0
        if (nv.getLuong() == null || nv.getLuong().compareTo(BigDecimal.ZERO) == 0) {
            if ("Quản lý".equalsIgnoreCase(nv.getChucVu())) {
                nv.setLuong(new BigDecimal("15000000"));
            } else {
                nv.setLuong(new BigDecimal("8000000"));
            }
        }
        
        if (nv.getTrangThai() == null) {
            nv.setTrangThai("Đang làm việc");
        }

        return ResponseEntity.ok(repo.save(nv));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody NhanVien updated) {
        NhanVien nv = repo.findById(id).orElseThrow();
        nv.setHoTen(updated.getHoTen());
        nv.setNgaySinh(updated.getNgaySinh());
        nv.setGioiTinh(updated.getGioiTinh());
        nv.setDiaChi(updated.getDiaChi());
        nv.setSdt(updated.getSdt());
        nv.setChucVu(updated.getChucVu());
        nv.setLuong(updated.getLuong());
        nv.setCaLamViec(updated.getCaLamViec());
        nv.setTrangThai(updated.getTrangThai());
        
        // Tính lương tự động cơ bản theo chức vụ nếu thay đổi chức vụ và lương không đổi (có thể cần logic phức tạp hơn, tạm thời giữ nguyên lương user gửi lên)
        return ResponseEntity.ok(repo.save(nv));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        NhanVien nv = repo.findById(id).orElse(null);
        if (nv != null) {
            // Xóa nhân viên trước
            repo.deleteById(id);
            // Xóa tài khoản nhân viên
            TaiKhoan tk = taiKhoanRepo.findByUserName(id);
            if (tk != null) {
                taiKhoanRepo.delete(tk);
            }
        }
        return ResponseEntity.ok(Map.of("message", "Đã cho nghỉ việc và xóa dữ liệu nhân viên"));
    }
}
