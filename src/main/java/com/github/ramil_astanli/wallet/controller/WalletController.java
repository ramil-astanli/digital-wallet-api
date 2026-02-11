package com.github.ramil_astanli.wallet.controller;

import com.github.ramil_astanli.wallet.dto.request.TransferRequestDTO;
import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.entity.CustomUserDetails;
import com.github.ramil_astanli.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> create(@Valid @RequestBody WalletRequestDTO request) {
        WalletResponseDTO response = walletService.createWallet(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getAllWallets(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<WalletResponseDTO> response = walletService.getAllWallets(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO request) {
        TransferResponseDTO response = walletService.transferMoney(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{walletId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionByWalletId(@PathVariable Long walletId) {
        List<TransactionResponseDTO> response = walletService.getTransactionByWalletId(walletId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<WalletResponseDTO> restore(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.restoreWallet(id));
    }
}