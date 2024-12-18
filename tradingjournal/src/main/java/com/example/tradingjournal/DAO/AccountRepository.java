package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.AccountDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountDTO, Long> {
    @Query(value = "SELECT * FROM accounts WHERE accountName = :accountName", nativeQuery = true)
    Optional<AccountDTO> findByAccountName(String accountName);

    @Query(value = "SELECT * FROM accounts WHERE user_id = :id", nativeQuery = true)
    List<Optional<AccountDTO>> findAccountByUserId(long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM accounts WHERE accountName = :accountName", nativeQuery = true)
    int deleteByAccountName(String accountName);
}
