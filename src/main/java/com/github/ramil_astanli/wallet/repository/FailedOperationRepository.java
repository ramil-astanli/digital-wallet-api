package com.github.ramil_astanli.wallet.repository;

import com.github.ramil_astanli.wallet.auth.entity.FailedOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedOperationRepository extends JpaRepository<FailedOperation, Long> {
    List<FailedOperation> findByOperationType(String operationType);
}