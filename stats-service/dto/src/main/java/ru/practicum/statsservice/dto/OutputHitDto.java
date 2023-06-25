package ru.practicum.statsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputHitDto {
    String app;
    String uri;
    long hits;
}
