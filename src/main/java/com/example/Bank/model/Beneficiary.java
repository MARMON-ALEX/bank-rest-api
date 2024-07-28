package com.example.Bank.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "beneficiaries")
@Data
@NoArgsConstructor
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false)
    private String name; // Имя бенефициара

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Account> accounts; // Список аккаунтов (OneToMany)
}
