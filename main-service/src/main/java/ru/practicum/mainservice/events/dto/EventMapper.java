package ru.practicum.mainservice.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.events.Event;

@UtilityClass
public class EventMapper {
    public Event fromNewEventDto(NewEventDto newEventDto, long initiatorId) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .categoryId(newEventDto.getCategory())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiatorId(initiatorId)
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }
}
