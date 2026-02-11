package com.github.ramil_astanli.wallet.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";
}