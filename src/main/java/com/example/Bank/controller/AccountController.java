package com.example.Bank.controller;

import com.example.Bank.DTO.CreateAccountByIdRequest;
import com.example.Bank.DTO.CreateAccountByNameRequest;
import com.example.Bank.model.Account;
import com.example.Bank.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/beneficiary/{beneficiaryId}")
    public List<Account> getAccountsByBeneficiaryId(@PathVariable Long beneficiaryId) {
        return accountService.getAccountsByBeneficiaryId(beneficiaryId);
    }

    @PostMapping("/create/by-name")
    public Account createAccount(@RequestBody @Valid CreateAccountByNameRequest request) {
        return accountService.createAccount(request.getName(), request.getPinCode());
    }

    @PostMapping("/create/by-id")
    public Account createAccount(@RequestBody @Valid CreateAccountByIdRequest request) {
        return accountService.createAccount(request.getBeneficiaryId(), request.getPinCode());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/beneficiary/{beneficiaryId}")
    public ResponseEntity<Void> deleteAccountsByBeneficiaryId(@PathVariable Long beneficiaryId) {
        accountService.deleteAccountsByBeneficiaryId(beneficiaryId);
        return ResponseEntity.noContent().build();
    }
}
