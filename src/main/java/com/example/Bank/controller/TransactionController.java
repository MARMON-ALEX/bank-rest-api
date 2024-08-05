package com.example.Bank.controller;

import com.example.Bank.dto.DepositRequest;
import com.example.Bank.dto.TransferRequest;
import com.example.Bank.dto.WithdrawRequest;
import com.example.Bank.model.Account;
import com.example.Bank.model.Transaction;
import com.example.Bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransactionsByAccountId(@PathVariable String accountNumber) {
        return transactionService.getTransactionsByAccountId(accountNumber);
    }

    @PostMapping("/deposit")
    public Account deposit(@RequestBody @Valid DepositRequest depositRequest) {
        return transactionService.deposit(depositRequest);
    }


    @PostMapping("/withdraw")
    public Account withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest) {
        return transactionService.withdraw(withdrawRequest);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody @Valid TransferRequest transferRequest) {
        transactionService.transfer(transferRequest);
        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
    }
}
