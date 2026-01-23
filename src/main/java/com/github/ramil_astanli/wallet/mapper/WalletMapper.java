package com.github.ramil_astanli.wallet.mapper;

import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.entity.Transaction;
import com.github.ramil_astanli.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component // Spring-in bu klası tanıması üçün
public class WalletMapper {

    // DTO-dan Entity-yə (Yaratmaq üçün)
    public Wallet toEntity(WalletRequestDTO dto) {
        if (dto == null) return null;

        return Wallet.builder()
                .ownerName(dto.getOwnerName())
                .balance(dto.getBalance())
                .currency(dto.getCurrency())
                .build();
    }

    // Entity-dən DTO-ya (Göstərmək üçün)
    public WalletResponseDTO toResponseDTO(Wallet entity) {
        if (entity == null) return null;

        return WalletResponseDTO.builder()
                .id(entity.getId())
                .ownerName(entity.getOwnerName())
                .balance(entity.getBalance())
                .currency(entity.getCurrency())
                .build();
    }

    // WalletMapper.java daxilinə əlavə et:

    public TransactionResponseDTO toTransactionResponse(Transaction entity) {
        if (entity == null) return null;

        return TransactionResponseDTO.builder()
                .id(entity.getId())
                .fromWalletId(entity.getFromWalletId())
                .toWalletId(entity.getToWalletId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public TransferResponseDTO toTransferResponse(Wallet fromWallet, Wallet toWallet, BigDecimal amount) {
        return TransferResponseDTO.builder()
                .transactionId(java.util.UUID.randomUUID().toString())
                .status("SUCCESS")
                .amount(amount)
                .fromOwner(fromWallet.getOwnerName())
                .toOwner(toWallet.getOwnerName())
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}