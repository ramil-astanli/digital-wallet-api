package com.github.ramil_astanli.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;

import java.math.BigDecimal;

@Entity // Bu klasın bir verilənlər bazası cədvəli olduğunu bildirir
@Table(name = "wallets") // Cədvəlin adını bazada "wallets" olaraq təyin edirik
@Getter // Bütün field-lər üçün getter-ləri avtomatik yaradır
@Setter // Bütün field-lər üçün setter-ləri avtomatik yaradır
@NoArgsConstructor // Parametrsiz constructor (Hibernate üçün mütləqdir)
@AllArgsConstructor // Bütün field-ləri daxil edən constructor
@Builder // Obyekti daha rahat yaratmaq üçün (Design Pattern)
@SQLDelete(sql = "UPDATE wallets SET active = false WHERE id = ?") // VACİB HİSSƏ
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID-ni bazada avtomatik artan (1, 2, 3...) edir
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false) // Boş qala bilməz
    private String ownerName;

    @NotNull(message = "Balance cannot be null or negative")
    @Column(nullable = false)
    private BigDecimal balance; // Pul məbləğləri üçün həmişə BigDecimal istifadə edirik (dəqiqlik üçün)

    @NotBlank(message = "Currency must be noted")
    @Column(nullable = false)
    private String currency; // Məsələn: "AZN", "USD"

    @Builder.Default
    private boolean active = true;
}