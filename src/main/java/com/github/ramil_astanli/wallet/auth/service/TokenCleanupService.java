package com.github.ramil_astanli.wallet.auth.service;

import com.github.ramil_astanli.wallet.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void removeExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        System.out.println("Köhnəlmiş tokenlər təmizləndi.");
    }
}