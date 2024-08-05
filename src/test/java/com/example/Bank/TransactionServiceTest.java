package com.example.Bank;

import com.example.Bank.dto.DepositRequest;
import com.example.Bank.dto.TransferRequest;
import com.example.Bank.dto.WithdrawRequest;
import com.example.Bank.exeption.InsufficientFundsException;
import com.example.Bank.exeption.InvalidPinException;
import com.example.Bank.model.Account;
import com.example.Bank.model.Transaction;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.TransactionRepository;
import com.example.Bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionsByAccountId_ShouldReturnTransactions_WhenAccountNumberExists() {
        String accountNumber = "1234567890";
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = List.of(transaction1, transaction2);

        when(transactionRepository.findByAccount_AccountNumber(accountNumber)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountNumber);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByAccount_AccountNumber(accountNumber);
    }

    @Test
    void getTransactionsByAccountId_ShouldReturnEmptyList_WhenNoTransactionsExistForAccountNumber() {
        String accountNumber = "1234567890";

        when(transactionRepository.findByAccount_AccountNumber(accountNumber)).thenReturn(Collections.emptyList());

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountNumber);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByAccount_AccountNumber(accountNumber);
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenAccountExists() {
        DepositRequest depositRequest = new DepositRequest("1234567890", new BigDecimal("100.00"));
        Account account = new Account();
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("200.00"));

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = transactionService.deposit(depositRequest);

        assertNotNull(updatedAccount);
        assertEquals(new BigDecimal("300.00"), updatedAccount.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deposit_ShouldThrowException_WhenAccountNotFound() {
        DepositRequest depositRequest = new DepositRequest("1234567890", new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.deposit(depositRequest);
        });

        assertEquals("No deposit account found", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldDecreaseBalance_WhenAccountExistsAndPinIsValidAndSufficientFunds() {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                "1234567890",
                new BigDecimal("100.00"),
                "1234");

        Account account = new Account();
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("200.00"));
        account.setPinCode("1234");

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = transactionService.withdraw(withdrawRequest);

        assertNotNull(updatedAccount);
        assertEquals(new BigDecimal("100.00"), updatedAccount.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldThrowException_WhenAccountNotFound() {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                "1234567890",
                new BigDecimal("100.00"),
                "1234");

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.withdraw(withdrawRequest);
        });

        assertEquals("No withdrawal account found", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldThrowException_WhenPinIsInvalid() {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                "1234567890",
                new BigDecimal("100.00"),
                "1234");

        Account account = new Account();
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("200.00"));
        account.setPinCode("0000");

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        InvalidPinException exception = assertThrows(InvalidPinException.class, () -> {
            transactionService.withdraw(withdrawRequest);
        });

        assertEquals("Invalid PIN code", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                "1234567890",
                new BigDecimal("300.00"),
                "1234");

        Account account = new Account();
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("200.00"));
        account.setPinCode("1234");

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.withdraw(withdrawRequest);
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldTransferAmount_WhenAccountsExistAndPinIsValidAndSufficientFunds() {
        TransferRequest transferRequest = new TransferRequest(
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                "1234");

        Account fromAccount = new Account();
        fromAccount.setAccountNumber("1234567890");
        fromAccount.setBalance(new BigDecimal("200.00"));
        fromAccount.setPinCode("1234");

        Account toAccount = new Account();
        toAccount.setAccountNumber("0987654321");
        toAccount.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(toAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        transactionService.transfer(transferRequest);

        assertEquals(new BigDecimal("100.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("150.00"), toAccount.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }
}
