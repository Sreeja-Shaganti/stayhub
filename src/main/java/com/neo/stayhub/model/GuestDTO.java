package com.neo.stayhub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GuestDTO {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String fullName;

    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 50)
    private String idProofType;

    @Size(max = 100)
    private String idProofNumber;

    @NotNull
    @JsonProperty("isPrimaryGuest")
    private Boolean isPrimaryGuest;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long booking;

}
