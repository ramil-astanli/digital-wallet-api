package com.github.ramil_astanli.wallet.config;

import com.github.ramil_astanli.wallet.auth.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRegisteredEvent {

    private final User user;

}