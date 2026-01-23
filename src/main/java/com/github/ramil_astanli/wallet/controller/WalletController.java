package com.github.ramil_astanli.wallet.controller;

import com.github.ramil_astanli.wallet.dto.request.TransferRequestDTO;
import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // 1. Yeni cüzdan yaradılması
    @PostMapping
    public ResponseEntity<WalletResponseDTO> create(@Valid @RequestBody WalletRequestDTO request) {
        WalletResponseDTO response = walletService.createWallet(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Bütün cüzdanların siyahısı
    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getAll() {
        List<WalletResponseDTO> response = walletService.getAllWallets();
        return ResponseEntity.ok(response);
    }

    // 5. ID-yə görə cüzdanı tapmaq
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

    // 4. Bütün əməliyyat tarixçəsi (Transaction history)
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> response = walletService.getAllTransactions();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<WalletResponseDTO> restore(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.restoreWallet(id));
    }
}