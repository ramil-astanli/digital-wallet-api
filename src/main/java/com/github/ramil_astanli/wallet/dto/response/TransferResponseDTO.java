package com.github.ramil_astanli.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponseDTO {
    private String transactionId;
    private String status;
    private BigDecimal amount;
    private String fromOwner;
    private String toOwner;
    private LocalDateTime timestamp;
}