package com.example.tradingjournal;

import com.example.tradingjournal.DTO.TradeDTO;

import java.util.Optional;

public interface ServiceInterface <T> {

    T save(T object);

    Optional<T> findByName(String name);

    Iterable<T> findAll();

    int deleteByName(String name);
}
