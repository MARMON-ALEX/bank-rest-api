package com.example.Bank.controller;

import com.example.Bank.DTO.DepositRequest;
import com.example.Bank.DTO.TransferRequest;
import com.example.Bank.DTO.WithdrawRequest;
import com.example.Bank.model.Account;
import com.example.Bank.model.Transaction;
import com.example.Bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{accountId}")
    public List<Transaction> getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @PostMapping("/deposit")
    public Account deposit(@RequestBody @Valid DepositRequest depositRequest) {
        return transactionService.deposit(depositRequest.getAccountId(), depositRequest.getAmount());
    }

    @PostMapping("/withdraw")
    public Account withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest) {
        return transactionService.withdraw(withdrawRequest.getAccountId(), withdrawRequest.getAmount(), withdrawRequest.getPinCode());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody @Valid TransferRequest transferRequest) {
        transactionService.transfer(transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount(), transferRequest.getPinCode());
        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
    }
}
