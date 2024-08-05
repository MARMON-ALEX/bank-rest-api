package com.example.Bank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WithdrawRequest {
    @NotNull(message = "Account ID is mandatory")
    private String accountNumber;

    @NotNull(message = "Amount is mandatory")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    @NotBlank(message = "PinCode is mandatory")
    @Size(min = 4, max = 4, message = "PinCode must be 4 digits")
    private String pinCode;
}
