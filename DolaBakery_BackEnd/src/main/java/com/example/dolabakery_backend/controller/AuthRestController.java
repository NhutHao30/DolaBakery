package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.Models.TaiKhoan;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import com.example.dolabakery_backend.repository.TaiKhoanRepository;
import com.example.dolabakery_backend.service.OTP_Email;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private OTP_Email emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpSession session) {
        String username = request.get("username");
        String password = request.get("password");

        TaiKhoan tk = taiKhoanRepository.findByUserName(username);

        // Hỗ trợ cả mật khẩu đã hash và mật khẩu cũ chưa hash để tránh lỗi cho tài khoản cũ
        boolean isPasswordMatch = false;
        if (tk != null) {
            if (tk.getPassword() != null && tk.getPassword().startsWith("$2a$")) {
                isPasswordMatch = passwordEncoder.matches(password, tk.getPassword());
            } else {
                isPasswordMatch = password.equals(tk.getPassword());
            }
        }

        if (tk == null || !isPasswordMatch) {
            return ResponseEntity.badRequest().body(Map.of("error", "Sai tài khoản hoặc mật khẩu!"));
        }

        // Lưu session
        session.setAttribute("user", tk);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng nhập thành công!");
        response.put("role", tk.getMaRole());
        response.put("username", tk.getUserName());
        return ResponseEntity.ok(response);
    }

    // API LẤY THÔNG TIN SESSION HIỆN TẠI
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", tk.getUserName());
        response.put("role", tk.getMaRole());
        
        KhachHang kh = khachHangRepository.findByUserName(tk.getUserName());
        if (kh != null) {
            response.put("fullName", kh.getHoTen());
            response.put("email", tk.getEmail());
        }
        
        return ResponseEntity.ok(response);
    }

    // API ĐĂNG XUẤT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công!"));
    }

    private String generateMaKH() {
        long count = khachHangRepository.count() + 1;
        return String.format("KH%03d", count);
    }

    // API ĐĂNG KÝ
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String repassword = request.get("repassword");
        String hoTen = request.get("HOTEN");
        String gioiTinh = request.get("GioiTinh");
        String sdt = request.get("SDT");
        String email = request.get("EMAIL");

        if (!password.equals(repassword)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mật khẩu nhập lại không chính xác!"));
        }

        if (taiKhoanRepository.findByUserName(username) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tên đăng nhập đã tồn tại!"));
        }

        if (taiKhoanRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email đã tồn tại!"));
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setUserName(username);
        tk.setPassword(passwordEncoder.encode(password));
        tk.setEmail(email);
        tk.setMaRole(2); // Role 2 là Khách hàng mặc định
        taiKhoanRepository.save(tk);

        KhachHang kh = new KhachHang();
        kh.setMaKH(generateMaKH());
        kh.setHoTen(hoTen);
        kh.setSdt(sdt);
        kh.setDiaChi("");
        kh.setDiemTichLuy(0);
        kh.setNgaySinh(null);
        kh.setGioiTinh(gioiTinh);
        kh.setUserName(username);
        khachHangRepository.save(kh);

        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!"));
    }

    // API QUÊN MẬT KHẨU
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vui lòng cung cấp email!"));
        }

        TaiKhoan tk = taiKhoanRepository.findByEmail(email);
        if (tk == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email không tồn tại trong hệ thống!"));
        }

        // Generate 6 digit OTP
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));
        
        // Save OTP in session to verify later
        session.setAttribute("otp", otp);
        session.setAttribute("reset_email", email);

        // Send OTP
        try {
            emailService.sendOTP(email, otp);
            return ResponseEntity.ok(Map.of("message", "Mã OTP khôi phục đã được gửi đến email của bạn."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi gửi email: " + e.getMessage()));
        }
    }

    // API ĐỔI MẬT KHẨU KHI QUÊN
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (email == null || otp == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vui lòng cung cấp đủ thông tin!"));
        }

        String sessionOtp = (String) session.getAttribute("otp");
        String sessionEmail = (String) session.getAttribute("reset_email");

        if (sessionOtp == null || sessionEmail == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phiên xác thực đã hết hạn hoặc chưa yêu cầu gửi mã!"));
        }

        if (!sessionEmail.equals(email) || !sessionOtp.equals(otp)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mã OTP không chính xác!"));
        }

        TaiKhoan tk = taiKhoanRepository.findByEmail(email);
        if (tk == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email không tồn tại trong hệ thống!"));
        }

        tk.setPassword(passwordEncoder.encode(newPassword));
        taiKhoanRepository.save(tk);

        // Xóa session sau khi thành công
        session.removeAttribute("otp");
        session.removeAttribute("reset_email");

        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công! Bạn có thể đăng nhập bằng mật khẩu mới."));
    }

    // =========================================================
    // GOOGLE AUTHENTICATION INTEGRATION
    // =========================================================

    // API check trạng thái Google Login (React sẽ gọi API này sau khi Google chuyển hướng về frontend)
    @GetMapping("/google-status")
    public ResponseEntity<?> googleStatus(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk != null) {
            return ResponseEntity.ok(Map.of("status", "success", "username", tk.getUserName(), "role", tk.getMaRole()));
        }
        
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");
        
        if (email != null) {
            return ResponseEntity.ok(Map.of("status", "need_info", "email", email, "name", name));
        }
        
        return ResponseEntity.badRequest().body(Map.of("error", "Không có thông tin Google trong session"));
    }

    // API hoàn tất đăng ký cho Google Login
    @PostMapping("/google-register")
    public ResponseEntity<?> googleRegister(@RequestBody Map<String, String> request, HttpSession session) {
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");
        
        if (email == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phiên đăng ký hết hạn. Vui lòng đăng nhập lại."));
        }

        String sdt = request.get("sdt");
        String gioiTinh = request.get("gioiTinh");

        if (sdt == null || !sdt.matches("\\d{10}")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vui lòng nhập số điện thoại đủ 10 chữ số!"));
        }

        String username = email.split("@")[0];
        // Ensure username is unique
        int suffix = 1;
        String finalUsername = username;
        while(taiKhoanRepository.findByUserName(finalUsername) != null) {
            finalUsername = username + suffix;
            suffix++;
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setUserName(finalUsername);
        tk.setPassword(null); // OAuth accounts don't need password
        tk.setEmail(email);
        tk.setMaRole(2);
        taiKhoanRepository.save(tk);

        KhachHang kh = new KhachHang();
        kh.setMaKH(generateMaKH());
        kh.setHoTen(name);
        kh.setSdt(sdt);
        kh.setDiaChi("");
        kh.setDiemTichLuy(0);
        kh.setNgaySinh(null);
        kh.setGioiTinh(gioiTinh);
        kh.setUserName(finalUsername);
        khachHangRepository.save(kh);

        session.setAttribute("user", tk);
        session.removeAttribute("email");
        session.removeAttribute("name");

        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!", "username", tk.getUserName()));
    }
}
