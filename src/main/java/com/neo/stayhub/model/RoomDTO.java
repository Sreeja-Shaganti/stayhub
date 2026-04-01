package com.neo.stayhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String roomNo;

    private Integer floorNo;

    @NotNull
    @Size(max = 30)
    private String status;

    @NotNull
    private Boolean smokingAllowed;

    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal priceOverride;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

    @NotNull
    private Long hotel;

    @NotNull
    private Long roomType;

    private List<Long> roomAmenityAmenities;

}
