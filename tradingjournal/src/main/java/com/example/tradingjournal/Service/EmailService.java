package com.example.tradingjournal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendOTP(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Your OTP Code for My Trading Journal");
        message.setText("Your OTP code is: " + otp);

        javaMailSender.send(message);
    }

    public void sendWelcomeEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Welcome to My Trading Journal");
        message.setText("Welcome to My Trading Journal");

        javaMailSender.send(message);
    }

    public void sendUserAccountDeletedEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("My Trading Journal Account Deleted");
        message.setText("Your My Trading Journal account has been deleted and all the data from our system is removed.");

        javaMailSender.send(message);
    }
}
