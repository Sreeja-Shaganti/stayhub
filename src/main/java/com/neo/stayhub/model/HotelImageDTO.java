package com.neo.stayhub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HotelImageDTO {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String imageUrl;

    @Size(max = 255)
    private String altText;

    @NotNull
    @JsonProperty("isCover")
    private Boolean isCover;

    @NotNull
    private Integer displayOrder;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long hotel;

}
