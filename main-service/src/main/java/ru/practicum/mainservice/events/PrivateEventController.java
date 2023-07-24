package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.events.dto.EventMapper;
import ru.practicum.mainservice.events.dto.input.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.events.dto.input.NewEventDto;
import ru.practicum.mainservice.events.dto.input.UpdateEventUserRequest;
import ru.practicum.mainservice.events.dto.output.EventFullDto;
import ru.practicum.mainservice.events.dto.output.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.events.dto.output.EventShortDto;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;
import ru.practicum.mainservice.participationRequest.ParticipationRequestService;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService service;
    private final ParticipationRequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto,
                                    @PathVariable long userId) {
        log.info("Request received POST /users/{}/events with body {}", userId, newEventDto);
        return EventMapper.toEventFullDto(
                service.createEvent(EventMapper.fromNewEventDto(newEventDto, userId)));
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET /users/{}/events?from={}&size={}", userId, from, size);
        return EventMapper.toEventShortDtoList(
                service.getUserEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable long userId,
                                     @PathVariable long eventId) {
        log.info("Request received GET /users/{}/events/{}", userId, eventId);
        return EventMapper.toEventFullDto(
                service.getUserEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        log.info("Request received PATCH /users/{}/events/{} with body {}", userId, eventId, updateEvent);
        updateEvent.setId(eventId);
        return EventMapper.toEventFullDto(
                service.updateUserEvent(
                        EventMapper.fromUpdateEventUserRequest(updateEvent, userId),
                        updateEvent.getStateAction()));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("Request received GET /users/{}/events/{}/requests", userId, eventId);
        return ParticipationRequestMapper.toParticipationRequestDtoList(
                requestService.getEventParticipationRequests(userId, eventId));
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequestStatus(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Request received PATCH /users/{}/events/{}/requests with body {}",
                userId,
                eventId,
                statusUpdateRequest);
        List<List<ParticipationRequest>> result = requestService.updateParticipationRequestStatus(
                userId,
                eventId,
                statusUpdateRequest.getRequestIds(),
                statusUpdateRequest.getStatus());
        return EventMapper.toEventRequestStatusUpdateResult(result.get(0), result.get(1));
    }
}
