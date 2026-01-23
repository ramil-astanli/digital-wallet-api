package com.github.ramil_astanli.wallet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromWalletId; // Göndərən
    private Long toWalletId;   // Alan
    private BigDecimal amount;  // Məbləğ

    private String description; // Məsələn: "Borc", "Hədiyyə"
    private LocalDateTime timestamp; // Nə vaxt baş verib?
}