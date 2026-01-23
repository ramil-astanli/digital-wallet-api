package com.github.ramil_astanli.wallet.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletRequestDTO {
    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotNull(message = "Initial balance is required")
    @Min(value = 0, message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotBlank(message = "Currency is required (e.g. AZN, USD)")
    private String currency;
}