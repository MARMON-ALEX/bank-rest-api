package com.example.Bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Связь с аккаунтом (ManyToOne)

    @Column(nullable = false)
    private BigDecimal amount; // Сумма транзакции

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // Тип транзакции

    @Column(nullable = false)
    private LocalDateTime timestamp; // Время транзакции

    public Transaction(Account account, BigDecimal amount, TransactionType transactionType, LocalDateTime timestamp) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }
}
