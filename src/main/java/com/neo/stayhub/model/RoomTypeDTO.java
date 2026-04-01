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
public class RoomTypeDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    private Integer capacityAdults;

    @NotNull
    private Integer capacityChildren;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal basePrice;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal extraBedPrice;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

}
