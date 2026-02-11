package com.github.ramil_astanli.wallet.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDTO {

    @NotNull(message = "Initial balance is required")
    @Min(value = 0, message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotBlank(message = "Currency is required (e.g. AZN, USD)")
    private String currency;
}