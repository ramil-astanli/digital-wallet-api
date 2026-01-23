package com.github.ramil_astanli.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class WalletResponseDTO {
    private Long id;
    private String ownerName;
    private BigDecimal balance;
    private String currency;
}