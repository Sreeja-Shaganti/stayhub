package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRoleDTO {

    private Long id;

    @NotNull
    private OffsetDateTime assignedAt;

    @NotNull
    private Long user;

    @NotNull
    private Long role;

}
