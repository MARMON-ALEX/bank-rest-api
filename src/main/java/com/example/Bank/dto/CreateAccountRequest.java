package com.example.Bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountRequest {
    private Long beneficiaryId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "PinCode is mandatory")
    @Size(min = 4, max = 4, message = "PinCode must be 4 digits")
    private String pinCode;
}
