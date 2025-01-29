package com.example.tradingjournal.DTO;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String preferredLanguage;

    @OneToMany(mappedBy = "userDTO", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountDTO> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "userDTO", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenDTO> refreshTokens = new ArrayList<>();

    public UserDTO() {}

    public UserDTO(String userName, String emailAddress, String password, String preferredLanguage) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.preferredLanguage = preferredLanguage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @PrePersist
    public void setDefaultPreferredLanguage() {
        if(preferredLanguage == null) {
            this.preferredLanguage = "English";
        }
    }

    public void addAccount(AccountDTO account) {
        accounts.add(account);
        account.setUserDTO(this);
    }

    public void removeAccount(AccountDTO account) {
        accounts.remove(account);
        account.setUserDTO(null);
    }

    public void addRefreshToken(RefreshTokenDTO refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setUser(this);
    }

    public void removeRefreshToken(RefreshTokenDTO refreshToken) {
        refreshTokens.remove(refreshToken);
        refreshToken.setUser(null);
    }
}
