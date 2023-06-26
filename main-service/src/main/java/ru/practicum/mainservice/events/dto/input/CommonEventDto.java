package ru.practicum.mainservice.events.dto.input;

import lombok.Data;
import ru.practicum.mainservice.events.dto.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public abstract class CommonEventDto {
    @NotBlank(message = "The field annotation can not be blank")
    @Size(min = 20, max = 2000, message = "Annotation must be longer then 20 and shorter then 2000 characters")
    private String annotation;

    @NotNull(message = "Event must have category")
    private Long category;

    @NotBlank(message = "The field description can not be blank")
    @Size(min = 20, max = 7000, message = "Description must be longer then 20 and shorter then 7000 characters")
    private String description;

    @NotNull(message = "The field eventDate can not be null")
    @Future
    private Timestamp eventDate;

    @NotNull(message = "The field location can not be null")
    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank(message = "The field title can not be blank")
    @Size(min = 3, max = 120, message = "Title must be longer then 3 and shorter then 120 characters")
    private String title;
}
