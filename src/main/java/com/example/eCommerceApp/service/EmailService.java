package com.example.eCommerceApp.service;

import com.example.eCommerceApp.model.Order;
import com.example.eCommerceApp.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOrderConfirmation(Order savedOrder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(savedOrder.getUser().getEmail());
        message.setSubject("Oder confirmation");
        message.setText("Your order has been confirmed. Order ID: " + savedOrder.getId());
        mailSender.send(message);
    }

    public void sendConfirmationCode(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Confirm your email");
        message.setText("Please confirm your email by entering this code " + user.getConfirmationCode());
        mailSender.send(message);
    }

}
