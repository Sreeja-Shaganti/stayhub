package com.neo.stayhub.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String type;

    @NotNull
    @Size(max = 200)
    private String subject;

    @NotNull
    private String message;

    @NotNull
    @Size(max = 30)
    private String deliveryChannel;

    @NotNull
    @Size(max = 30)
    private String status;

    private OffsetDateTime sentAt;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

}
