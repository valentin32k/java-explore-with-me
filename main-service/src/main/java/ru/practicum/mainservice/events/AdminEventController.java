package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.events.dto.EventMapper;
import ru.practicum.mainservice.events.dto.input.UpdateEventAdminRequest;
import ru.practicum.mainservice.events.dto.output.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService service;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@RequestBody @Valid UpdateEventAdminRequest updateEvent,
                                           @PathVariable Long eventId) {
        log.info("Request received PATCH /admin/events/{} with body {}", eventId, updateEvent);
        updateEvent.setId(eventId);
        return EventMapper.toEventFullDto(
                service.updateAdminEvent(
                        EventMapper.fromCommonUpdateEventDto(updateEvent),
                        updateEvent.getStateAction()));
    }

    @GetMapping
    public List<EventFullDto> getAdminEvents(@RequestParam(defaultValue = "") List<Long> users,
                                             @RequestParam(defaultValue = "") List<EventState> states,
                                             @RequestParam(defaultValue = "") List<Long> categories,
                                             @RequestParam(defaultValue = "1970-01-01 00:00:01") Timestamp rangeStart,
                                             @RequestParam(defaultValue = "2038-01-19 03:14:07") Timestamp rangeEnd,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request received GET /admin/events with: \n" +
                        "users = {},\n" +
                        "states = {},\n" +
                        "categories = {},\n" +
                        "rangeStart = {},\n" +
                        "rengeEnd = {},\n" +
                        "from = {},\n" +
                        "size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return EventMapper.toEventFullDtoList(
                service.getAdminEvents(
                        users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        from,
                        size));
    }
}
