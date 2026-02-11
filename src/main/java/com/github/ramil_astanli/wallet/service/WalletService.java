package com.github.ramil_astanli.wallet.service;

import com.github.ramil_astanli.wallet.auth.entity.FailedOperation;
import com.github.ramil_astanli.wallet.auth.entity.User;
import com.github.ramil_astanli.wallet.auth.repository.UserRepository;
import com.github.ramil_astanli.wallet.config.SecurityUtils;
import com.github.ramil_astanli.wallet.dto.request.TransferRequestDTO;
import com.github.ramil_astanli.wallet.dto.request.WalletRequestDTO;
import com.github.ramil_astanli.wallet.dto.response.TransactionResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.TransferResponseDTO;
import com.github.ramil_astanli.wallet.dto.response.WalletResponseDTO;
import com.github.ramil_astanli.wallet.entity.CustomUserDetails;
import com.github.ramil_astanli.wallet.entity.Transaction;
import com.github.ramil_astanli.wallet.entity.Wallet;
import com.github.ramil_astanli.wallet.mapper.WalletMapper;
import com.github.ramil_astanli.wallet.repository.FailedOperationRepository;
import com.github.ramil_astanli.wallet.repository.TransactionRepository;
import com.github.ramil_astanli.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletMapper walletMapper;
    private final UserRepository userRepository;
    private final FailedOperationRepository failedOperationRepository;
    private final EmailService emailService;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // Hər cəhddə YENİ tranzaksiya aç // Hər bir təkrar cəhd üçün yeni və təmiz bir tranzaksiya açır
    @Retryable(
            retryFor = { QueryTimeoutException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @CacheEvict(value = "all_wallets", allEntries = true)
    public void createWalletWithRetry(User user) {
        Wallet wallet = Wallet.builder()
                .user(user)
                .currency("AZN")
                .balance(BigDecimal.ZERO)
                .build();
        walletRepository.save(wallet);
    }

    @Recover
    public void recover(Exception e, User user) {
        log.error("KRİTİK: 3 cəhddən sonra cüzdan yaradıla bilmədi. User: {}", user.getEmail());

        try {
            FailedOperation failure = FailedOperation.builder()
                    .operationType("WALLET_CREATION")
                    .targetId(user.getEmail())
                    .errorLog(e.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();

            failedOperationRepository.save(failure);

            String errorDetail = "Xəta növü: " + e.getClass().getSimpleName() + " | Mesaj: " + e.getMessage();
            emailService.sendAdminAlert(user.getEmail(), errorDetail);

        } catch (Exception fatal) {
            log.error("Recover zamanı gözlənilməz xəta!", fatal);
        }
    }

    @Transactional
    @CacheEvict(value = "all_wallets", allEntries = true)
    @PreAuthorize("isAuthenticated()")
    public WalletResponseDTO createWallet(WalletRequestDTO dto) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletMapper.toEntity(dto);

        wallet.setUser(user);
        wallet.setActive(true);

        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponseDTO(savedWallet);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "all_wallets", key = "#userId")
    public List<WalletResponseDTO> getAllWallets(Long userId) {
        return walletRepository.findAllByActiveTrueAndUserId(userId)
                .stream()
                .map(walletMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponseDTO> getTransactionByWalletId(Long walletId) {
        return transactionRepository.findAllByFromWalletIdOrToWalletId(walletId,walletId)
                .stream()
                .map(walletMapper::toTransactionResponse)
                .toList();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "wallets", key = "#request.fromWalletId"),
            @CacheEvict(value = "wallets", key = "#request.toWalletId"),
            @CacheEvict(value = "all_wallets", allEntries = true)
    })
    @PreAuthorize("@walletService.isWalletOwner(#request.fromWalletId)")
    public TransferResponseDTO transferMoney(TransferRequestDTO request) {
        Wallet fromWallet = walletRepository.findByIdAndActiveTrue(request.getFromWalletId())
                .orElseThrow(() -> new RuntimeException("Göndərən tapılmadı"));
        Wallet toWallet = walletRepository.findByIdAndActiveTrue(request.getToWalletId())
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

    @Cacheable(value = "wallets", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    public WalletResponseDTO getById(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cüzdan tapılmadı: " + id));
        return walletMapper.toResponseDTO(wallet);
    }

    @CacheEvict(value = "wallets", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cüzdan tapılmadı"));

        walletRepository.delete(wallet);
    }

    @CacheEvict(value = "wallets", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public WalletResponseDTO restoreWallet(Long id) {
        Wallet wallet = walletRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new EntityNotFoundException("Cüzdan bazada heç yoxdur!"));

        wallet.setActive(true);
        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponseDTO(savedWallet);
    }

    @Cacheable(value = "wallet_owners", key = "{#walletId, T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()}")
    public boolean isWalletOwner(Long walletId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return walletRepository.findById(walletId)
                .map(wallet -> wallet.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }
}