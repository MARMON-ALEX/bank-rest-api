package com.example.Bank.repository;

import com.example.Bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBeneficiaryId(Long beneficiaryId);
}
