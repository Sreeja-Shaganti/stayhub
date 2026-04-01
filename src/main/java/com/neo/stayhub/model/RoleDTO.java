package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private Long version;

}
