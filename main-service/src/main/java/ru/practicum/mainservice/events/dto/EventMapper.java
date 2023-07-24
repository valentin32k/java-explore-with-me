package ru.practicum.mainservice.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.events.dto.input.CommonUpdateEventDto;
import ru.practicum.mainservice.events.dto.input.NewEventDto;
import ru.practicum.mainservice.events.dto.input.UpdateEventUserRequest;
import ru.practicum.mainservice.events.dto.output.EventFullDto;
import ru.practicum.mainservice.events.dto.output.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.events.dto.output.EventShortDto;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestMapper;
import ru.practicum.mainservice.users.dto.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    public Event fromNewEventDto(NewEventDto newEventDto, Long initiatorId) {
        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .categoryId(newEventDto.getCategory())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .views(0L)
                .initiatorId(initiatorId)
                .build();
        if (newEventDto.getLocation() != null) {
            event.setLat(newEventDto.getLocation().getLat());
            event.setLon(newEventDto.getLocation().getLon());
        }
        return event;
    }

    public Event fromCommonUpdateEventDto(CommonUpdateEventDto event) {
        Event newEvent = Event.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .categoryId(event.getCategory())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .views(0L)
                .build();
        if (event.getLocation() != null) {
            newEvent.setLat(event.getLocation().getLat());
            newEvent.setLon(event.getLocation().getLon());
        }
        return newEvent;
    }

    public Event fromUpdateEventUserRequest(UpdateEventUserRequest updateEvent, long ownerId) {
        Event event = fromCommonUpdateEventDto(updateEvent);
        event.setInitiatorId(ownerId);
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setViews(event.getViews());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setState(event.getState());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        return eventFullDto;
    }

    public List<EventFullDto> toEventFullDtoList(List<Event> events) {
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public EventShortDto toEventShortDto(Event event) {
        return toEventFullDto(event);
    }

    public List<EventShortDto> toEventShortDtoList(List<Event> events) {
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> confirmedRequests,
                                                                           List<ParticipationRequest> rejectedRequests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(ParticipationRequestMapper.toParticipationRequestDtoList(confirmedRequests));
        result.setRejectedRequests(ParticipationRequestMapper.toParticipationRequestDtoList(rejectedRequests));
        return result;
    }
}