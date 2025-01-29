package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.AuthenticationRepository;
import com.example.tradingjournal.DAO.UserRepository;
import com.example.tradingjournal.DTO.RefreshTokenDTO;
import com.example.tradingjournal.DTO.UserDTO;
import com.example.tradingjournal.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1296000000;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository, UserRepository userRepository) {
        this.authenticationRepository = authenticationRepository;
        this.userRepository = userRepository;
    }

    public Optional<UserDTO> authenticateUser(String email, String password) {
        return userRepository.validateUserLogin(email, password);
    }

    public Optional<UserDTO> isUserPresent(String email) {
        return userRepository.isUserPresent(email);
    }

    public void saveRefreshTokenToDB(UserDTO user, String refreshToken) {
        Optional<RefreshTokenDTO> refreshTokenDTOFromDB = authenticationRepository.findRefreshTokenDTOByEmail(user.getEmailAddress());

        if (refreshTokenDTOFromDB.isEmpty()) {
            LocalDateTime expiration = LocalDateTime.now().plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_TIME));// 15 days expiry
            RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
            refreshTokenDTO.setRefreshToken(refreshToken);
            refreshTokenDTO.setRefreshTokenExpiration(expiration);
            refreshTokenDTO.setUser(user);
            authenticationRepository.save(refreshTokenDTO);
        }
    }

    public Optional<RefreshTokenDTO> getRefreshToken(String email) {
        return authenticationRepository.findRefreshTokenDTOByEmail(email);
    }

    public RefreshTokenDTO validateRefreshToken(String token) {
        return authenticationRepository.findByRefreshToken(token)
                .filter(rt -> rt.getRefreshTokenExpiration().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));
    }

    public String renewAccessToken(String refreshToken) {
        RefreshTokenDTO validToken = validateRefreshToken(refreshToken);
        UserDTO user = validToken.getUser();
        return JwtUtil.generateToken(user.getEmailAddress()); // Replace with your JWT generation logic
    }

    public void invalidateToken(String refreshToken) {
        Optional<RefreshTokenDTO> refreshTokenDTO = authenticationRepository.findByRefreshToken(refreshToken);

        if (refreshTokenDTO.isPresent()) {
            authenticationRepository.delete(refreshTokenDTO.get());
            System.out.println("Refresh token invalidated successfully.");
        } else {
            throw new RuntimeException("Refresh token not found.");
        }
    }

}
