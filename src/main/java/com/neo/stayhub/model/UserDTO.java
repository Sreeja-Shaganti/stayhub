package com.neo.stayhub.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String fullName;

    @NotNull
    @Size(max = 150, min = 2)
    @Email
    private String email;

    @Size(max = 20)
    private String phone;

    @NotNull
    @Size(max = 255)
    private String passwordHash;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean accountLocked;

    @NotNull
    private Boolean emailVerified;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long version;

}
