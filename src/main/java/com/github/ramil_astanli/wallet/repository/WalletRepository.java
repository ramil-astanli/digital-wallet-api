package com.github.ramil_astanli.wallet.repository;

import com.github.ramil_astanli.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllByActiveTrueAndUserId(Long userId);

    Optional<Wallet> findByIdAndActiveTrue(Long id);

    @Query(value = "SELECT * FROM wallets WHERE id = :id", nativeQuery = true)
    Optional<Wallet> findByIdIncludingDeleted(@Param("id") Long id);
}