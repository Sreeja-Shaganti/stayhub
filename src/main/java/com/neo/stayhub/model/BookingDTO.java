package com.neo.stayhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingDTO {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String bookingNo;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    @NotNull
    private Integer adults;

    @NotNull
    private Integer children;

    @NotNull
    @Size(max = 30)
    private String status;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal subtotal;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxAmount;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal discountAmount;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalAmount;

    private String specialRequests;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

    @NotNull
    private Long user;

    @NotNull
    private Long hotel;

}
