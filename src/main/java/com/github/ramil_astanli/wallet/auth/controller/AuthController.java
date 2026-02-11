package com.github.ramil_astanli.wallet.auth.controller;

import com.github.ramil_astanli.wallet.auth.dto.request.LoginRequest;
import com.github.ramil_astanli.wallet.auth.dto.request.LogoutRequest;
import com.github.ramil_astanli.wallet.auth.dto.request.RefreshRequest;
import com.github.ramil_astanli.wallet.auth.dto.request.RegisterRequest;
import com.github.ramil_astanli.wallet.auth.dto.response.AuthResponse;
import com.github.ramil_astanli.wallet.auth.dto.response.TokenResponse;
import com.github.ramil_astanli.wallet.auth.service.AuthService;
import com.github.ramil_astanli.wallet.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest,
                                                         HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()){
            ip = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");

        AuthResponse response = authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                ip,
                userAgent,
                request
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, @RequestBody RefreshRequest refreshRequest) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        TokenResponse response = refreshTokenService.rotateToken(
                refreshRequest.getRefreshToken(), ip, userAgent
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getRefreshToken());

        return ResponseEntity.ok("Sistemdən uğurla çıxış edildi. Sessiya sonlandırıldı.");
    }
}