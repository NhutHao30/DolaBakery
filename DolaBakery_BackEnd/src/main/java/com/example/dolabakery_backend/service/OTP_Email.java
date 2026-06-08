package com.example.dolabakery_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OTP_Email {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOTP(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP đổi mật khẩu");
        message.setText("Mã OTP của bạn: " + otp);
        mailSender.send(message);
    }
}
