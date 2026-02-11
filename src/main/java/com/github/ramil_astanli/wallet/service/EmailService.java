package com.github.ramil_astanli.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendAdminAlert(String userEmail, String errorMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ramilastanli19@gmail.com");
        message.setSubject("KRİTİK: Cüzdan Yaradılması Uğursuz Oldu");
        message.setText("İstifadəçi: " + userEmail + "\nXəta mesajı: " + errorMessage);
        
        mailSender.send(message);
    }
}