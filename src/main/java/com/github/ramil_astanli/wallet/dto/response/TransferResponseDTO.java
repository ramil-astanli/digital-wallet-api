package com.github.ramil_astanli.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponseDTO {
    private String transactionId; // Əməliyyatın unikal kodu (məs: UUID)
    private String status;        // SUCCESS, FAILED
    private BigDecimal amount;
    private String fromOwner;     // Göndərənin adı
    private String toOwner;       // Alanın adı
    private LocalDateTime timestamp;
}