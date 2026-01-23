package com.github.ramil_astanli.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE wallets SET active = false WHERE id = ?")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String ownerName;

    @NotNull(message = "Balance cannot be null or negative")
    @Column(nullable = false)
    private BigDecimal balance;

    @NotBlank(message = "Currency must be noted")
    @Column(nullable = false)
    private String currency;

    @Builder.Default
    private boolean active = true;
}