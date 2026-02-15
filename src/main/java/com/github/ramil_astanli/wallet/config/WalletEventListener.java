package com.github.ramil_astanli.wallet.config;

import com.github.ramil_astanli.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class WalletEventListener {

    private final WalletService walletService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Event tutuldu, cüzdan yaradılması servisinə göndərilir...");
        walletService.createWalletWithRetry(event.getUser());
    }
}
