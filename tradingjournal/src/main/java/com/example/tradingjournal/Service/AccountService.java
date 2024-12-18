package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.AccountRepository;
import com.example.tradingjournal.DAO.UserRepository;
import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.DTO.TransactionsDTO;
import com.example.tradingjournal.HardCodedValues;
import com.example.tradingjournal.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements ServiceInterface<AccountDTO> {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountDTO save(AccountDTO accountDTO) {
        return accountRepository.save(accountDTO);
    }

    public List<AccountDTO> saveAll(List<AccountDTO> accountDTO) {
        return accountRepository.saveAll(accountDTO);
    }

    @Override
    public Optional<AccountDTO> findByName(String name) {
        return accountRepository.findByAccountName(name);
    }

    public Optional<AccountDTO> findByAccountId(long id) {
        return accountRepository.findById(id);
    }

    public List<Optional<AccountDTO>> findAccountsByUserId(long id) {
        return accountRepository.findAccountByUserId(id);
    }

    @Override
    public Iterable<AccountDTO> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public int deleteByName(String userName) {
        return accountRepository.deleteByAccountName(userName);
    }

    public List<Optional<AccountDTO>> setCorrectPrimaryAccountForUser(AccountDTO newAccount) throws  ResponseStatusException{

        //Fetch all Accounts for User Associated with the UserID
        List<Optional<AccountDTO>> existingAccounts = findAccountsByUserId(newAccount.getUserDTO().getUserId());

        //Logic to Check if at least one account per user should be Primary
        if(existingAccounts.isEmpty() && !newAccount.getIsPrimaryAccount())
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.ATLEAST_ONE_PRIMARY_ACCOUNT);
        }
        else if(newAccount.getIsPrimaryAccount())
        {
            for(Optional<AccountDTO> a : existingAccounts)
                a.get().setIsPrimaryAccount(false);

            existingAccounts.add(Optional.of(newAccount));
            return existingAccounts;
        }
        else{
            existingAccounts.add(Optional.of(newAccount));
            return existingAccounts;
        }
    }

    public boolean checkIfDuplicateAccountName(AccountDTO newAccount) {

        //Fetch all Accounts for User Associated with the UserID
        List<Optional<AccountDTO>> existingAccounts = findAccountsByUserId(newAccount.getUserDTO().getUserId());

        if(existingAccounts.isEmpty())
            return false;
        else
        {
            for(Optional<AccountDTO> a : existingAccounts)
            {
                if(a.get().getAccountName().equalsIgnoreCase(newAccount.getAccountName()))
                    return true;
            }
        }
        return false;
    }

    public void checkIfTransactionsArePresent(AccountDTO newAccount) throws ResponseStatusException {
        List<TransactionsDTO> transactions = newAccount.getTransactions(newAccount);
            if(transactions.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.AT_LEAST_ONE_TRANSACTION_SHOULD_BE_PRESENT_WHEN_CREATING_NEW_ACCOUNT);

            if(newAccount.getAccountID() == 0 && transactions.get(0).getType().equalsIgnoreCase(HardCodedValues.Strings.WITHDRAWAL))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,HardCodedValues.Strings.FIRST_TRANSACTION_CANNOT_BE_A_WITHDRAWAL_TRANSACTION);

    }

    public double calculateTotalAccountBalance(AccountDTO newAccount)
    {
        double totalAmount = newAccount.getAccountBalance();
        List<TransactionsDTO> transactions = newAccount.getTransactions(newAccount);
        if(newAccount.getAccountID() == 0)
        {
            for(TransactionsDTO t : transactions)
            {
                if(t.getType().equalsIgnoreCase(HardCodedValues.Strings.DEPOSIT))
                    totalAmount += t.getAmount();
                else if(t.getType().equalsIgnoreCase(HardCodedValues.Strings.WITHDRAWAL))
                    totalAmount -= t.getAmount();
            }

        }
        return totalAmount;
    }
}
