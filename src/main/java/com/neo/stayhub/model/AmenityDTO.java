package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AmenityDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    private OffsetDateTime createdAt;

}
