package com.example.tradingjournal.Service;

import jakarta.persistence.Column;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class OTPService {

    public String generateOTP() {
        return RandomStringUtils.randomNumeric(6); // 6-digit OTP
    }
}
