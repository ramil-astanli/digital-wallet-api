package com.github.ramil_astanli.wallet.mapper;

import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.entity.Transaction;
import com.github.ramil_astanli.wallet.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", ignore = true)
    Wallet toEntity(WalletRequestDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFirstName", source = "user.firstName")
    @Mapping(target = "userLastName", source = "user.lastName")
    WalletResponseDTO toResponseDTO(Wallet entity);

    TransactionResponseDTO toTransactionResponse(Transaction entity);

    @Mapping(target = "transactionId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "SUCCESS")
    @Mapping(target = "fromOwner", source = "fromWallet.user.email")
    @Mapping(target = "toOwner", source = "toWallet.user.email")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    TransferResponseDTO toTransferResponse(Wallet fromWallet, Wallet toWallet, BigDecimal amount);
}