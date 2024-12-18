package com.example.tradingjournal.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Transactions")
public class TransactionsDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionID;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime transactionDateTime;

    @Column(nullable = false)
    private double amount;

    private String note;

    @ManyToOne
    @JoinColumn(name = "accountid", nullable = false) // Foreign key column
    private AccountDTO accountDTO;

    public TransactionsDTO(long transactionID, String type, LocalDateTime transactionDateTime, double amount, String note, AccountDTO accountDTO) {
        this.transactionID = transactionID;
        this.type = type;
        this.transactionDateTime = transactionDateTime;
        this.amount = amount;
        this.note = note;
        this.accountDTO = accountDTO;
    }

    public TransactionsDTO() {

    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }
}
