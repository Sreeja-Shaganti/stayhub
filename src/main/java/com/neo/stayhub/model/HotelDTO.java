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
public class HotelDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @NotNull
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @NotNull
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @NotNull
    @Size(max = 100)
    private String country;

    @Size(max = 20)
    private String postalCode;

    @Digits(integer = 10, fraction = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal latitude;

    @Digits(integer = 10, fraction = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal longitude;

    @Digits(integer = 2, fraction = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal starRating;

    @NotNull
    @Size(max = 30)
    private String status;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

}
