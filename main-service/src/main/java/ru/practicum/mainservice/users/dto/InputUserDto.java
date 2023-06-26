package ru.practicum.mainservice.users.dto;

import lombok.Value;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class InputUserDto {
    @NotBlank(message = "The field name can not be blank")
    @Size(max = 255, message = "Name must be shorter than 255 characters")
    String name;

    @Email(message = "The field email is incorrect ")
    @NotEmpty(message = "The field email can not be empty")
    @Size(min = 2, max = 255, message = "Name must be longer then 2 and shorter then 255 characters")
    @Column(name = "email", unique = true)
    String email;
}
