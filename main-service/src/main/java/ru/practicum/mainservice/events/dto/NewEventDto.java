package ru.practicum.mainservice.events.dto;

import lombok.Value;
import ru.practicum.mainservice.events.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Value
public class NewEventDto {

    @NotBlank(message = "The field annotation can not be blank")
    @Size(min = 20, max = 2000, message = "Annotation must be longer then 20 and shorter then 2000 characters")
    String annotation;

    @NotNull(message = "Event must have category")
    Long category;

    @NotBlank(message = "The field description can not be blank")
    @Size(min = 20, max = 7000, message = "Description must be longer then 20 and shorter then 7000 characters")
    String description;

    @NotNull(message = "The field eventDate can not be null")
    @Future
    Timestamp eventDate;

    @NotNull(message = "The field location can not be null")
    Location location;

    @NotNull(message = "Paid cannot be null")
    Boolean paid = false;

    @NotNull(message = "The field participantLimit cannot be null")
    Integer participantLimit = 0;

    @NotNull(message = "The field requestModeration cannot be null")
    Boolean requestModeration = true;

    @NotBlank(message = "The field title can not be blank")
    @Size(min = 3, max = 120, message = "Title must be longer then 3 and shorter then 120 characters")
    String title;
}
