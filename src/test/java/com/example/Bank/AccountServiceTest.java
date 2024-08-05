package com.example.Bank;
import com.example.Bank.dto.CreateAccountRequest;
import com.example.Bank.exeption.AccountNotFoundException;
import com.example.Bank.model.Account;
import com.example.Bank.model.Beneficiary;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BeneficiaryRepository;
import com.example.Bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_ShouldReturnAccount_WhenBeneficiaryExists() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setBeneficiaryId(1L);
        request.setName("Rick Sanchez");
        request.setPinCode("1234");

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(1L);
        beneficiary.setName("Rick Sanchez");

        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        Account account = accountService.createAccount(request);

        assertNotNull(account);
        assertEquals("1234", account.getPinCode());
        assertEquals(beneficiary, account.getBeneficiary());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrowExceptionWhenBeneficiaryIdNotFound() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setBeneficiaryId(1L);
        request.setName("Rick Sanchez");
        request.setPinCode("1234");

        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(request);
        });

        assertEquals("beneficiary with this ID does not exist", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(beneficiaryRepository, never()).save(any(Beneficiary.class));
    }

    @Test
    void createAccount_ShouldCreateAccountWithNewBeneficiaryWhenIdIsNull() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setBeneficiaryId(null);
        request.setName("Rick Sanchez");
        request.setPinCode("1234");

        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenAnswer(i -> i.getArguments()[0]);

        Account account = accountService.createAccount(request);

        assertNotNull(account);
        assertEquals("1234", account.getPinCode());
        assertEquals("Rick Sanchez", account.getBeneficiary().getName());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts_WhenBeneficiaryIdIsNull() {
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> allAccounts = Arrays.asList(account1, account2);

        when(accountRepository.findAll()).thenReturn(allAccounts);

        List<Account> result = accountService.getAllAccounts(null);

        assertEquals(allAccounts, result);
        verify(accountRepository, times(1)).findAll();
        verify(beneficiaryRepository, never()).existsById(anyLong());
        verify(accountRepository, never()).findByBeneficiaryId(anyLong());
    }

    @Test
    void getAllAccounts_ShouldThrowException_WhenBeneficiaryIdNotFound() {
        Long beneficiaryId = 1L;

        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(false);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAllAccounts(beneficiaryId);
        });

        assertEquals("Beneficiary with ID " + beneficiaryId + " not found", exception.getMessage());
        verify(accountRepository, never()).findAll();
        verify(beneficiaryRepository, times(1)).existsById(beneficiaryId);
        verify(accountRepository, never()).findByBeneficiaryId(anyLong());
    }


    @Test
    void getAllAccounts_ShouldReturnAccounts_WhenBeneficiaryIdExists() {
        Long beneficiaryId = 1L;
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> beneficiaryAccounts = Arrays.asList(account1, account2);

        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(true);
        when(accountRepository.findByBeneficiaryId(beneficiaryId)).thenReturn(beneficiaryAccounts);

        List<Account> result = accountService.getAllAccounts(beneficiaryId);

        assertEquals(beneficiaryAccounts, result);
        verify(accountRepository, never()).findAll();
        verify(beneficiaryRepository, times(1)).existsById(beneficiaryId);
        verify(accountRepository, times(1)).findByBeneficiaryId(beneficiaryId);
    }
}
