package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.TradeRepository;
import com.example.tradingjournal.DTO.TradeDTO;
import com.example.tradingjournal.ServiceInterface;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TradeService implements ServiceInterface<TradeDTO> {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }
    @Override
    public TradeDTO save(TradeDTO trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public Optional<TradeDTO> findByName(String name) {
        return tradeRepository.findByMarket(name);
    }

    @Override
    public Iterable<TradeDTO> findAll() {
        return tradeRepository.findAll();
    }

    @Override
    public int deleteByName(String userName)
    {
        return tradeRepository.deleteByMarket(userName);
    }
}
