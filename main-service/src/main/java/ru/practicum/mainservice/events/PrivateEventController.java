package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.events.dto.EventMapper;
import ru.practicum.mainservice.events.dto.NewEventDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService service;

    @PostMapping
    public String createEvent(@Valid @RequestBody NewEventDto newEventDto,
                              @PathVariable long userId) {
        log.info("Request received POST /users/{}/events with body {}", userId, newEventDto);
        return service.createEvent(EventMapper.fromNewEventDto(newEventDto, userId));
    }
}
