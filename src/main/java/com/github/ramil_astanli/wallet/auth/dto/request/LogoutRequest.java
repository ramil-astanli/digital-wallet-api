package com.github.ramil_astanli.wallet.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
    
    @NotBlank(message = "Refresh token boş ola bilməz!")
    private String refreshToken;
}