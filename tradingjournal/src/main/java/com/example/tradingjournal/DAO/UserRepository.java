package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO, Long> {

    @Query(value = "SELECT * FROM users WHERE user_name = :userName", nativeQuery = true)
    Optional<UserDTO> findByUserName(String userName);

    @Query(value = "SELECT * FROM users WHERE email_address = :email AND password = :password", nativeQuery = true)
    Optional<UserDTO> validateUserLogin(String email, String password);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users WHERE user_name = :userName", nativeQuery = true)
    int deleteByUserName(String userName);
}
