package com.github.ramil_astanli.wallet.repository;

import com.github.ramil_astanli.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Müəyyən bir cüzdanın bütün əməliyyatlarını görmək üçün:
    List<Transaction> findByFromWalletIdOrToWalletId(Long fromId, Long toId);
}