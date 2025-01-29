package com.example.tradingjournal.DTO;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiration;

    @Column(nullable = false, updatable = false)
    private LocalDateTime refreshTokenCreatedAt;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "emailAddress", nullable = false)
    private UserDTO userDTO;

    // Default constructor
    public RefreshTokenDTO() {
    }

    // Parameterized constructor
    public RefreshTokenDTO(UserDTO userDTO, String refreshToken, LocalDateTime refreshTokenExpiration) {
        this.userDTO = userDTO;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(LocalDateTime refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public LocalDateTime getRefreshTokenCreatedAt() {
        return refreshTokenCreatedAt;
    }

    public void setRefreshTokenCreatedAt(LocalDateTime refreshTokenCreatedAt) {
        this.refreshTokenCreatedAt = refreshTokenCreatedAt;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // Set createdAt before persisting
    @PrePersist
    protected void onCreate() {
        this.refreshTokenCreatedAt = LocalDateTime.now();
    }
}