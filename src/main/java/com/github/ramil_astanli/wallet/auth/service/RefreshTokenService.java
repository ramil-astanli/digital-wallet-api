package com.github.ramil_astanli.wallet.auth.service;

import com.github.ramil_astanli.wallet.auth.dto.response.TokenResponse;
import com.github.ramil_astanli.wallet.auth.entity.RefreshToken;
import com.github.ramil_astanli.wallet.auth.entity.User;
import com.github.ramil_astanli.wallet.auth.repository.RefreshTokenRepository;
import com.github.ramil_astanli.wallet.auth.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Transactional
    public TokenResponse rotateToken(String oldToken, String currentIp, String currentUserAgent) {
        return refreshTokenRepository.findByToken(oldToken)
                .map(token -> {
                    // Validasiyalar (Expiry və Fingerprint)
                    verifySecurity(token, currentIp, currentUserAgent);

                    User user = token.getUser();

                    // KÖHNƏNİ SİL
                    refreshTokenRepository.delete(token);

                    // YENİSİNİ BİRBAŞA DİGƏR METODLA YARAT (Təkrarçılıq bitdi!)
                    RefreshToken newToken = authService.createRefreshToken(user.getId(), currentIp, currentUserAgent);

                    String accessToken = jwtUtils.generateTokenFromUsername(createDetails(user));

                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newToken.getToken())
                            .expiresIn(refreshTokenDurationMs)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Token reuse detected!"));
    }

    private UserDetails createDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

    private void verifySecurity(RefreshToken token, String ip, String ua) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Expired");
        }
        if (!token.getIpAddress().equals(ip) || !token.getUserAgent().equals(ua)) {
            refreshTokenRepository.deleteByUser(token.getUser());
            throw new RuntimeException("Security Breach!");
        }
    }
}