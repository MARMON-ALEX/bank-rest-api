package com.example.Bank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction(Account account, BigDecimal amount, TransactionType transactionType, LocalDateTime timestamp) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }
}
