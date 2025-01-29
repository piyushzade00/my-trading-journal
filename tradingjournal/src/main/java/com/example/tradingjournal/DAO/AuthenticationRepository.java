package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.RefreshTokenDTO;
import com.example.tradingjournal.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<RefreshTokenDTO, Long> {
    Optional<RefreshTokenDTO> findByRefreshToken(String token);

    @Query(value = "SELECT * FROM refresh_tokens WHERE user_email = :email", nativeQuery = true)
    Optional<RefreshTokenDTO> findRefreshTokenDTOByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshTokenDTO r WHERE r.refreshTokenExpiration < :currentTime")
    void deleteAllExpiredTokens(Timestamp currentTime);
}