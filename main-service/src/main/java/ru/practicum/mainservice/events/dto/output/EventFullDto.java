package ru.practicum.mainservice.events.dto.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.events.EventState;
import ru.practicum.mainservice.events.dto.Location;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class EventFullDto extends EventShortDto {
    private String description;
    private Integer participantLimit;
    private EventState state;
    private Timestamp createdOn;
    private Location location;
    private Boolean requestModeration;
    private Timestamp publishedOn;
}
