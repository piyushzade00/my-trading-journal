package com.example.tradingjournal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class TradingJournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingJournalApplication.class, args);
	}

}