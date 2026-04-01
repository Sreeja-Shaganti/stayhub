package com.neo.stayhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String gateway;

    @Size(max = 100)
    private String gatewayOrderId;

    @Size(max = 100)
    private String gatewayPaymentId;

    @Size(max = 100)
    private String paymentReference;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    @Size(max = 30)
    private String status;

    private OffsetDateTime paidAt;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

    @NotNull
    private Long booking;

}
