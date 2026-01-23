package com.github.ramil_astanli.wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequestDTO {

    @NotNull(message = "Göndərən cüzdanın ID-si boş ola bilməz")
    private Long fromWalletId;

    @NotNull(message = "Alan cüzdanın ID-si boş ola bilməz")
    private Long toWalletId;

    @NotNull(message = "Məbləğ mütləq qeyd edilməlidir")
    @DecimalMin(value = "0.01", message = "Minimum 0.01 məbləğində transfer edə bilərsiniz")
    private BigDecimal amount;

    private String note; // Könüllü qeyd (məsələn: "Borc", "Yemək pulu")
}