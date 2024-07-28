package com.example.Bank.service;

import com.example.Bank.model.Account;
import com.example.Bank.model.Beneficiary;
import com.example.Bank.model.exeption.AccountNotFoundException;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, BeneficiaryRepository beneficiaryRepository) {
        this.accountRepository = accountRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Account createAccount(String name, String pinCode) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(name);
        beneficiaryRepository.save(beneficiary);

        return accountRepository.save(createAccount(beneficiary, pinCode));
    }

    public Account createAccount(Long beneficiaryId, String pinCode) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary with ID " + beneficiaryId + " not found"));

        return accountRepository.save(createAccount(beneficiary, pinCode));
    }

    private Account createAccount(Beneficiary beneficiary, String pinCode) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setPinCode(pinCode);
        account.setBeneficiary(beneficiary);

        return account;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByBeneficiaryId(Long beneficiaryId) {
        if (!beneficiaryRepository.existsById(beneficiaryId)) {
            throw new IllegalArgumentException("Beneficiary with ID " + beneficiaryId + " not found");
        }
        return accountRepository.findByBeneficiaryId(beneficiaryId);
    }

    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }
        accountRepository.deleteById(accountId);
    }

    public void deleteAccountsByBeneficiaryId(Long beneficiaryId) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(beneficiaryId);
        if (accounts.isEmpty()) {
            throw new IllegalArgumentException("No accounts found for Beneficiary with ID " + beneficiaryId);
        }
        accountRepository.deleteAll(accounts);
    }

    private String generateAccountNumber() {
        // Логика генерации уникального номера счета
        return String.valueOf(System.currentTimeMillis());
    }

}
