package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {

    private Long id;

    @NotNull
    private Integer rating;

    private String comment;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long booking;

    @NotNull
    private Long hotel;

    @NotNull
    private Long user;

}
