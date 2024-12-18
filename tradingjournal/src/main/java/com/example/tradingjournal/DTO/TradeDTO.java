package com.example.tradingjournal.DTO;

import jakarta.persistence.*;

@Entity
@Table(name = "trades")
public class TradeDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tradeID;

    @Column(nullable = false)
    private String market;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double stopLoss;

    private String tags;
    private String notes;
    private int confidence;
    private String mistakes;

    public TradeDTO() {}

    public TradeDTO(long tradeID, String market, String symbol, String notes, String tags, double stopLoss,  int confidence) {
        this.tradeID = tradeID;
        this.market = market;
        this.symbol = symbol;
        this.notes = notes;
        this.tags = tags;
        this.stopLoss = stopLoss;
        this.confidence = confidence;
    }

    public long getTradeID() {
        return tradeID;
    }

    public void setTradeID(long tradeID) {
        this.tradeID = tradeID;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
}
