package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO, Long> {

    @Query(value = "SELECT * FROM users WHERE email_address = :email", nativeQuery = true)
    Optional<UserDTO> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE user_name = :name", nativeQuery = true)
    Optional<UserDTO> findByName(String name);

    @Query(value = "SELECT * FROM users WHERE email_address = :email AND password = :password", nativeQuery = true)
    Optional<UserDTO> validateUserLogin(String email, String password);

    @Query(value = "SELECT * FROM users WHERE email_address = :email", nativeQuery = true)
    Optional<UserDTO> isUserPresent(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users set password = :password WHERE email_address = :email", nativeQuery = true)
    int changePassword(String email, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users set user_name = :userName, preferred_language = :preferredLanguage WHERE email_address = :email", nativeQuery = true)
    int updateUserInfo(String userName, String preferredLanguage, String email);
}
