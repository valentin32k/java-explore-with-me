package ru.practicum.mainservice.categories.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class NewCategoryDto {
    long id;
    @NotBlank(message = "The field name can not be blank")
    @Size(min = 1, max = 50, message = "Name must be longer then 1 and shorter then 50 characters")
    String name;
}
