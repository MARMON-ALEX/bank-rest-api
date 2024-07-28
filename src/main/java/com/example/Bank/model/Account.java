package com.example.Bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(unique = true, nullable = false)
    private String accountNumber; // Уникальный номер счета

    @Column(nullable = false)
    private BigDecimal balance; // Баланс счета

    @Column(nullable = false)
    private String pinCode; // PIN-код для операций

    @ManyToOne
    @JsonIgnoreProperties("accounts")
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary; // Связь с бенефициаром (ManyToOne)
}