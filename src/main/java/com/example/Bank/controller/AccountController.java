package com.example.Bank.controller;

import com.example.Bank.dto.CreateAccountRequest;
import com.example.Bank.model.Account;
import com.example.Bank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts(@RequestParam(required = false) Long beneficiaryId) {
        return accountService.getAllAccounts(beneficiaryId);
    }

    @PostMapping("/create")
    public Account createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return accountService.createAccount(request);
    }
}
