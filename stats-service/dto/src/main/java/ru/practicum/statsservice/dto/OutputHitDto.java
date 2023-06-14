package ru.practicum.statsservice.dto;

import lombok.Value;

@Value
public class OutputHitDto {
    String app;
    String uri;
    long hits;
}
