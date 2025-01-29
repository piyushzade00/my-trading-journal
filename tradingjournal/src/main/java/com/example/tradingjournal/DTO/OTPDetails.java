package com.example.tradingjournal.DTO;

import java.time.LocalDateTime;

public class OTPDetails {
    private String otp;
    private LocalDateTime timestamp;

    public OTPDetails(String otp, LocalDateTime timestamp) {
        this.otp = otp;
        this.timestamp = timestamp;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
