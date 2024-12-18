package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.TransactionsDTO;
import com.example.tradingjournal.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/viewTransaction", method = RequestMethod.GET)
    public ResponseEntity<TransactionsDTO> viewTransactionByID(@RequestParam("id") Long id) {
        Optional<TransactionsDTO> transaction = transactionService.findById(id);
        if(transaction.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(transaction.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/viewAllTransactions", method = RequestMethod.GET)
    public ResponseEntity<Iterable<TransactionsDTO>> viewAllTransactions() {
        Iterable<TransactionsDTO> transactions = transactionService.findAll();
        if(transactions.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/viewAllTransactionsByAccountId", method = RequestMethod.GET)
    public ResponseEntity<List<TransactionsDTO>> viewAllTransactionsByAccountID(@RequestParam("accountId") Long accountId) {
        List<TransactionsDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        if(transactions.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/saveTransaction", method = RequestMethod.POST)
    public ResponseEntity<List<TransactionsDTO>> saveTransaction(@RequestBody List<TransactionsDTO> transactions)
    {
        transactionService.validateInputsForTransaction(transactions);
        List<TransactionsDTO> savedTransaction = transactionService.saveAll(transactions);
        return ResponseEntity.status(HttpStatus.OK).body(savedTransaction);
    }

    @RequestMapping(value = "/deleteTransaction", method = RequestMethod.GET)
    public ResponseEntity<String> deleteTransactionByID(@RequestParam("id") Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Transaction Deleted");
    }
}
