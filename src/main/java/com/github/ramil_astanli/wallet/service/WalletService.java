package com.github.ramil_astanli.wallet.service;

import com.github.ramil_astanli.wallet.dto.request.TransferRequestDTO;
import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.entity.Transaction;
import com.github.ramil_astanli.wallet.entity.Wallet;
import com.github.ramil_astanli.wallet.mapper.WalletMapper;
import com.github.ramil_astanli.wallet.repository.TransactionRepository;
import com.github.ramil_astanli.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletMapper walletMapper;

    public WalletResponseDTO createWallet(WalletRequestDTO dto) {
        Wallet wallet = walletMapper.toEntity(dto);

        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponseDTO(savedWallet);
    }

    public List<WalletResponseDTO> getAllWallets() {
        return walletRepository.findAllByActiveTrue()
                .stream()
                .map(walletMapper::toResponseDTO)
                .toList();
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(walletMapper::toTransactionResponse)
                .toList();
    }

    @Transactional
    public TransferResponseDTO transferMoney(TransferRequestDTO request) {
        Wallet fromWallet = walletRepository.findById(request.getFromWalletId())
                .orElseThrow(() -> new RuntimeException("Göndərən tapılmadı"));
        Wallet toWallet = walletRepository.findById(request.getToWalletId())
                .orElseThrow(() -> new RuntimeException("Alan tapılmadı"));

        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Kifayət qədər vəsait yoxdur!");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(request.getAmount()));
        toWallet.setBalance(toWallet.getBalance().add(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .fromWalletId(fromWallet.getId())
                .toWalletId(toWallet.getId())
                .amount(request.getAmount())
                .description(request.getNote())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        return walletMapper.toTransferResponse(fromWallet, toWallet, request.getAmount());
    }

    public WalletResponseDTO getById(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cüzdan tapılmadı: " + id));
        return walletMapper.toResponseDTO(wallet);
    }

    public void deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cüzdan tapılmadı"));

        walletRepository.delete(wallet);
    }

    @Transactional
    public WalletResponseDTO restoreWallet(Long id) {
        Wallet wallet = walletRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new EntityNotFoundException("Cüzdan bazada heç yoxdur!"));

        wallet.setActive(true);
        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponseDTO(savedWallet);
    }
}