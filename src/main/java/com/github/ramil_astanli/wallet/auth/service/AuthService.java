package com.github.ramil_astanli.wallet.auth.service;

import com.github.ramil_astanli.wallet.auth.dto.request.RegisterRequest;
import com.github.ramil_astanli.wallet.auth.dto.response.AuthResponse;
import com.github.ramil_astanli.wallet.auth.entity.RefreshToken;
import com.github.ramil_astanli.wallet.auth.entity.Role;
import com.github.ramil_astanli.wallet.auth.entity.User;
import com.github.ramil_astanli.wallet.auth.repository.RefreshTokenRepository;
import com.github.ramil_astanli.wallet.auth.repository.UserRepository;
import com.github.ramil_astanli.wallet.auth.security.JwtUtils;
import com.github.ramil_astanli.wallet.config.UserRegisteredEvent;
import com.github.ramil_astanli.wallet.entity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Xəta: Bu email ünvanı artıq istifadə olunub!");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(user));

        return "User registered";
    }

    public AuthResponse login(String email, String password, String ip, String userAgent, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateTokenFromUsername(userDetails);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = createRefreshToken(user.getId(), ip, userAgent);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public RefreshToken createRefreshToken(Long userId, String ip, String userAgent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .ipAddress(ip)
                .userAgent(userAgent)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logout(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}