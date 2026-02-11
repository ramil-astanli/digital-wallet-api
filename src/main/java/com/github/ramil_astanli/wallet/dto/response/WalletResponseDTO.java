package com.github.ramil_astanli.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class WalletResponseDTO {
    private Long id;
    private BigDecimal balance;
    private String currency;

    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
}