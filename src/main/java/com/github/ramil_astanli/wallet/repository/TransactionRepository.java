package com.github.ramil_astanli.wallet.repository;

import com.github.ramil_astanli.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByFromWalletIdOrToWalletId(Long fromWalletId,Long toWalletId);
}