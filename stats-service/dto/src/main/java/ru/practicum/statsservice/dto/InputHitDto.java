package ru.practicum.statsservice.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Value
public class InputHitDto {
    @NotBlank(message = "The field app can not be blanc")
    @Size(max = 255, message = "The app length must be shorter than 255 characters")
    String app;

    @NotBlank(message = "The field uri can not be blanc")
    @Size(max = 255, message = "The uri length must be shorter than 255 characters")
    String uri;

    @NotBlank(message = "The field ip can not be blanc")
    @Size(max = 15, message = "The ip length must be shorter than 15 characters")
    String ip;

    @NotNull(message = "The field Timestamp can not be null")
    @PastOrPresent(message = "The field timestamp can not be on the future")
    Timestamp timestamp;
}
