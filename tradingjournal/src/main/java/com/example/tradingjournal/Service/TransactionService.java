package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.AccountRepository;
import com.example.tradingjournal.DAO.TransactionRepository;
import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.DTO.TransactionsDTO;
import com.example.tradingjournal.HardCodedValues;
import com.example.tradingjournal.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ServiceInterface<TransactionsDTO> {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TradeService tradeService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService, TradeService tradeService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.tradeService = tradeService;
    }

    @Override
    public TransactionsDTO save(TransactionsDTO transactionDTO) {
        Optional<AccountDTO> account = this.accountRepository.findById(transactionDTO.getAccountDTO().getAccountID());
        if (account.isPresent()) {
            transactionDTO.setAccountDTO(account.get());
        }
        return transactionRepository.save(transactionDTO);
    }

    public List<TransactionsDTO> saveAll(List<TransactionsDTO> transactionDTO) {
        for(TransactionsDTO transaction : transactionDTO) {
            Optional<AccountDTO> account = this.accountRepository.findById(transaction.getAccountDTO().getAccountID());
            if (account.isPresent()) {
                transaction.setAccountDTO(account.get());
            }
        }

        return transactionRepository.saveAll(transactionDTO);
    }

    public Optional<TransactionsDTO> findByName(String name) {
       return Optional.empty();
    }

    public Optional<TransactionsDTO> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<TransactionsDTO> getTransactionsByAccountId(long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    public Iterable<TransactionsDTO> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public int deleteByName(String name) {
        return 0;
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void validateInputsForTransaction(List<TransactionsDTO> transactionDTO) throws ResponseStatusException{
        for(TransactionsDTO transaction : transactionDTO) {
            Optional<AccountDTO> account = accountService.findByAccountId(transaction.getAccountDTO().getAccountID());

            if(transaction.getTransactionDateTime().isBefore(account.get().getAccountCreationDateTime()))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.TRANSACTION_DATETIME_CANNOT_BE_BEFORE_ACCOUNT_CREATION_DATETIME);
            else if(transaction.getTransactionDateTime().isAfter(LocalDateTime.now()))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.TRANSACTION_DATETIME_CANNOT_BE_AFTER_CURRENT_DATETIME);
            else if(transaction.getAmount() <= 0)
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO);
            else if(transaction.getType().equalsIgnoreCase(HardCodedValues.Strings.WITHDRAWAL))
            {
                if(transaction.getAmount() > account.get().getAccountBalance())
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HardCodedValues.Strings.WITHDRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_ACCOUNT_BALANCE);
            }
        }

    }
}
