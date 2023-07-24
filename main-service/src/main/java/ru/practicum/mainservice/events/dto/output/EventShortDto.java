package ru.practicum.mainservice.events.dto.output;

import lombok.Data;
import ru.practicum.mainservice.categories.Category;
import ru.practicum.mainservice.users.dto.UserShortDto;

import java.sql.Timestamp;

@Data
public class EventShortDto {
    private long id;
    private String title;
    private String annotation;
    private Category category;
    private Boolean paid;
    private Timestamp eventDate;
    private UserShortDto initiator;
    private Long confirmedRequests;
    private Long views;
}
