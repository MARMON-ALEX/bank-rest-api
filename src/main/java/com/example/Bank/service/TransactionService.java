package com.example.Bank.service;

import com.example.Bank.dto.DepositRequest;
import com.example.Bank.dto.TransferRequest;
import com.example.Bank.dto.WithdrawRequest;
import com.example.Bank.model.Account;
import com.example.Bank.model.Transaction;
import com.example.Bank.exeption.InsufficientFundsException;
import com.example.Bank.exeption.InvalidPinException;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<Transaction> getTransactionsByAccountId(String accountNumber) {
        return transactionRepository.findByAccount_AccountNumber(accountNumber);
    }

    @Transactional
    public Account deposit(DepositRequest depositRequest) {
        Account account = accountRepository.findByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("No deposit account found"));
        account.setBalance(account.getBalance().add(depositRequest.getAmount()));

        Transaction transaction = new Transaction(account,
                depositRequest.getAmount(),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now());

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(WithdrawRequest withdrawRequest) {
        Account account = accountRepository.findByAccountNumber(withdrawRequest.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("No withdrawal account found"));
        if (!account.getPinCode().equals(withdrawRequest.getPinCode())) {
            throw new InvalidPinException("Invalid PIN code");
        }
        if (account.getBalance().compareTo(withdrawRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(withdrawRequest.getAmount()));
        Transaction transaction = new Transaction(account,
                withdrawRequest.getAmount(),
                Transaction.TransactionType.WITHDRAWAL,
                LocalDateTime.now());

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(TransferRequest transferRequest) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                transferRequest.getFromAccountNumber(),
                transferRequest.getAmount(),
                transferRequest.getPinCode()
        );

        DepositRequest depositRequest = new DepositRequest(
                transferRequest.getToAccountNumber(),
                transferRequest.getAmount()
        );

        withdraw(withdrawRequest);
        deposit(depositRequest);
    }
}
