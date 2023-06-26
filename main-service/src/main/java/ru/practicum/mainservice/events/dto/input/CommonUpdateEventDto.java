package ru.practicum.mainservice.events.dto.input;

import lombok.Data;
import ru.practicum.mainservice.events.dto.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public abstract class CommonUpdateEventDto {
    private long id;
    @Size(min = 20, max = 2000, message = "Annotation must be longer then 20 and shorter then 2000 characters")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Description must be longer then 20 and shorter then 7000 characters")
    private String description;

    @Future
    private Timestamp eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit = 0;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Title must be longer then 3 and shorter then 120 characters")
    private String title;
}
