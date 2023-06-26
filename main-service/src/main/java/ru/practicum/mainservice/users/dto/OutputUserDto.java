package ru.practicum.mainservice.users.dto;

import lombok.Value;

@Value
public class OutputUserDto {
    long id;
    String name;
    String email;
}
