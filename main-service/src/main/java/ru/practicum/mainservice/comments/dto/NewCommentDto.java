package ru.practicum.mainservice.comments.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class NewCommentDto {
    long id;
    @NotBlank(message = "The field comment text cannot be blank")
    @Size(min = 1, max = 4095, message = "Comment text must be longer then 1 and shorter then 4095 characters")
    String text;
}