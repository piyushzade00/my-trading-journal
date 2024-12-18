package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.TransactionsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionsDTO, Long> {

    @Query(value = "SELECT * FROM transactions WHERE accountid = :accountId", nativeQuery = true)
    List<TransactionsDTO> findByAccountId(@Param("accountId") Long accountId);

}
