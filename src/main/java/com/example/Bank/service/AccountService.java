package com.example.Bank.service;

import com.example.Bank.dto.CreateAccountRequest;
import com.example.Bank.model.Account;
import com.example.Bank.model.Beneficiary;
import com.example.Bank.exeption.AccountNotFoundException;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Transactional
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Beneficiary beneficiary;

        if (createAccountRequest.getBeneficiaryId() != null) {
            Optional<Beneficiary> optionalBeneficiary
                    = beneficiaryRepository.findById(createAccountRequest.getBeneficiaryId());
            if (optionalBeneficiary.isPresent()
                    && optionalBeneficiary.get().getName().equals(createAccountRequest.getName())) {
                beneficiary = optionalBeneficiary.get();
            } else {
                throw new IllegalArgumentException("beneficiary with this ID does not exist");
            }
        } else {
            beneficiary = new Beneficiary();
            beneficiary.setName(createAccountRequest.getName());
            beneficiaryRepository.save(beneficiary);
        }
        return accountRepository.save(createAccount(beneficiary, createAccountRequest.getPinCode()));
    }

    private Account createAccount(Beneficiary beneficiary, String pinCode) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setPinCode(pinCode);
        account.setBeneficiary(beneficiary);

        return account;
    }

    public List<Account> getAllAccounts(Long beneficiaryId) {
        if (beneficiaryId == null)
            return accountRepository.findAll();
        if (!beneficiaryRepository.existsById(beneficiaryId)) {
            throw new AccountNotFoundException("Beneficiary with ID " + beneficiaryId + " not found");
        }
        return accountRepository.findByBeneficiaryId(beneficiaryId);
    }

    private String generateAccountNumber() {
        // Логика генерации уникального номера счета
        return String.valueOf(System.currentTimeMillis());
    }

}
