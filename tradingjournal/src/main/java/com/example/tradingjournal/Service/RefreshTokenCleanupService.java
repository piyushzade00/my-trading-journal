package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class RefreshTokenCleanupService {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    /**
     * Scheduled task to clean up expired refresh tokens.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void cleanupExpiredTokens() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        authenticationRepository.deleteAllExpiredTokens(currentTime);
        System.out.println("Expired refresh tokens cleaned up successfully.");
    }
}