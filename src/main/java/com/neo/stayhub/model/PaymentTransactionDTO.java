package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentTransactionDTO {

    private Long id;

    @Size(max = 150)
    private String gatewayTxnId;

    @NotNull
    @Size(max = 30)
    private String txnType;

    @NotNull
    @Size(max = 30)
    private String status;

    private String rawPayload;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long payment;

}
