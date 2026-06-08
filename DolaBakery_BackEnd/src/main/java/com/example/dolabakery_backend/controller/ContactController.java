package com.example.dolabakery_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lien-he")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<?> sendContactEmail(@RequestBody ContactRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("minhphuctranlu2005@gmail.com"); // Gửi về email của admin (chủ shop)
            message.setSubject("Yêu cầu giải đáp thắc mắc mới từ: " + request.getHoTen());
            message.setText("Bạn nhận được một yêu cầu giải đáp thắc mắc từ khách hàng:\n\n" +
                            "Họ và tên: " + request.getHoTen() + "\n" +
                            "Email liên hệ: " + request.getEmail() + "\n" +
                            "Số điện thoại: " + request.getDienThoai() + "\n\n" +
                            "Nội dung cần giải đáp:\n" + request.getNoiDung() + "\n\n" +
                            "--------------------------------------\n" +
                            "Hệ thống Dola Bakery.");
            
            mailSender.send(message);
            return ResponseEntity.ok("Gửi yêu cầu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    public static class ContactRequest {
        private String hoTen;
        private String email;
        private String dienThoai;
        private String noiDung;

        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getDienThoai() { return dienThoai; }
        public void setDienThoai(String dienThoai) { this.dienThoai = dienThoai; }

        public String getNoiDung() { return noiDung; }
        public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    }
}
