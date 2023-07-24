package ru.practicum.mainservice.users.dto;

import lombok.Value;

@Value
public class UserDto {
    long id;
    String name;
    String email;
}
