package com.example.Bank.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class DepositRequest {
    @NotNull(message = "Account ID is mandatory")
    private Long accountId;

    @NotNull(message = "Amount is mandatory")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;
}
