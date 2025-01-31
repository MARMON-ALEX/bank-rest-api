package com.example.Bank.repository;

import com.example.Bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBeneficiaryId(Long beneficiaryId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
