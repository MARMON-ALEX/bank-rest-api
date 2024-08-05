package com.example.Bank.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    @NotNull(message = "From Account ID is mandatory")
    private Long fromAccountId;

    @NotNull(message = "To Account ID is mandatory")
    private Long toAccountId;

    @NotNull(message = "Amount is mandatory")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    @NotBlank(message = "PinCode is mandatory")
    @Size(min = 4, max = 4, message = "PinCode must be 4 digits")
    private String pinCode;
}
