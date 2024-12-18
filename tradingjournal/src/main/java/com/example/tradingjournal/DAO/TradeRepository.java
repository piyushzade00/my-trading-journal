package com.example.tradingjournal.DAO;

import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.DTO.TradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<TradeDTO, Long> {
    @Query(value = "SELECT * FROM trades WHERE market = :market", nativeQuery = true)
    Optional<TradeDTO> findByMarket(String market); //Not implemented yet, just for removing errors

    @Query(value = "DELETE FROM trades WHERE market = :market", nativeQuery = true)
    int deleteByMarket(String market); //Not implemented yet, just for removing errors
}
