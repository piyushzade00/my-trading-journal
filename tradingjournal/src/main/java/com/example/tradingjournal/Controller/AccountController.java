package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.AccountDTO;
import com.example.tradingjournal.HardCodedValues;
import com.example.tradingjournal.Service.AccountService;
import com.example.tradingjournal.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/viewAccount", method = RequestMethod.GET)
    public ResponseEntity<AccountDTO> viewAccountByAccountName(@RequestParam("accountname") String accountName) {
        Optional<AccountDTO> account = accountService.findByName(accountName);
        if(account.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(account.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/viewAllAccounts", method = RequestMethod.GET)
    public ResponseEntity<Iterable<AccountDTO>> viewAllAccounts() {
        Iterable<AccountDTO> accounts = accountService.findAll();
        if(accounts.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/saveAccount", method = RequestMethod.POST)
    public ResponseEntity<List<AccountDTO>> saveAccount(@RequestBody AccountDTO account)
    {
        if(accountService.checkIfDuplicateAccountName(account))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, HardCodedValues.Strings.DUPLICATE_ACCOUNT_NAME);
        }

        if(account.getAccountID() == 0)
            accountService.checkIfTransactionsArePresent(account);

        account.setAccountCreationDateTime(LocalDateTime.now());

        account.setAccountBalance(accountService.calculateTotalAccountBalance(account));

        List<Optional<AccountDTO>> accounts = accountService.setCorrectPrimaryAccountForUser(account);
        List<AccountDTO> accountList = accounts.stream()
                .filter(Optional ::isPresent)
                .map(Optional::get)
                .toList();
        List<AccountDTO> savedAccount = accountService.saveAll(accountList);

        //Calling saveTransaction API
        TransactionController t = new TransactionController(transactionService);
        t.saveTransaction(account.getTransactions(account));

        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
    }

    @RequestMapping(value = "/deleteAccount", method = RequestMethod.GET)
    public ResponseEntity<String> deleteAccountByUserName(@RequestParam("username") String userName) {
        if(accountService.deleteByName(userName) > 0)
         return ResponseEntity.status(HttpStatus.OK).body("Account Deleted");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not Found");
    }
}
