package com.example.tradingjournal.DTO;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Accounts")
public class AccountDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountID;

    @Column(nullable = false)
    private String accountName;

    private boolean isPrimaryAccount;

    @Column(nullable = false)
    private double accountBalance;

    private LocalDateTime accountCreationDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private UserDTO userDTO;


    @OneToMany(mappedBy = "accountDTO", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionsDTO> transactions;


    public AccountDTO(long accountID, String accountName, boolean isPrimaryAccount, double accountBalance, UserDTO userDTO) {
        this.accountID = accountID;
        this.accountName = accountName;
        this.isPrimaryAccount = isPrimaryAccount;
        this.accountBalance = accountBalance;
        this.userDTO = userDTO;
    }

    public AccountDTO() {

    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean getIsPrimaryAccount() {
        return isPrimaryAccount;
    }

    public void setIsPrimaryAccount(boolean primaryAccount) {
        this.isPrimaryAccount = primaryAccount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void addTransaction(TransactionsDTO transaction) {
        transactions.add(transaction);
        transaction.setAccountDTO(this);
    }

    public void removeTransaction(TransactionsDTO transaction) {
        transactions.remove(transaction);
        transaction.setAccountDTO(null);
    }

    public List<TransactionsDTO> getTransactions(AccountDTO account)
    {
        return account.transactions;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public LocalDateTime getAccountCreationDateTime() {
        return accountCreationDateTime;
    }

    public void setAccountCreationDateTime(LocalDateTime accountCreationDateTime) {
        this.accountCreationDateTime = accountCreationDateTime;
    }
}
