package com.github.ramil_astanli.wallet.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String operationType;
    private String targetId;
    @Column(columnDefinition = "TEXT")
    private String errorLog;
    private LocalDateTime createdAt;
}