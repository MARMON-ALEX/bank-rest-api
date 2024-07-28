package com.example.Bank.service;

import com.example.Bank.model.Account;
import com.example.Bank.model.Transaction;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No deposit account found"));
        account.setBalance(account.getBalance().add(amount));

        Transaction transaction = new Transaction(account,
                amount,
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now());

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }
    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount, String pinCode) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No withdrawal account found"));
        if (!account.getPinCode().equals(pinCode)) {
            throw new IllegalArgumentException("Invalid PIN code");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        Transaction transaction = new Transaction(account,
                amount,
                Transaction.TransactionType.WITHDRAWAL,
                LocalDateTime.now());

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String pinCode) {
        withdraw(fromAccountId, amount, pinCode); // Снятие средств
        deposit(toAccountId, amount); // Внесение средств
    }
}
