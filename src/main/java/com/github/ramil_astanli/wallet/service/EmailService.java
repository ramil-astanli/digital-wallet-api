package com.github.ramil_astanli.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${app.admin.email}")
    private String adminEmail;
    private final JavaMailSender mailSender;

    public void sendAdminAlert(String userEmail, String errorMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("KRİTİK: Cüzdan Yaradılması Uğursuz Oldu");
        message.setText("İstifadəçi: " + userEmail + "\nXəta mesajı: " + errorMessage);
        
        mailSender.send(message);
    }
}