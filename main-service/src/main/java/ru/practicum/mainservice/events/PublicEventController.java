package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.events.dto.EventMapper;
import ru.practicum.mainservice.events.dto.output.EventFullDto;
import ru.practicum.mainservice.events.dto.output.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getPublicEvents(@RequestParam(defaultValue = "") @Min(1) @Max(7000) String text,
                                               @RequestParam (defaultValue = "") List<Long> categories,
                                               @RequestParam(defaultValue = "") Boolean paid,
                                               @RequestParam(defaultValue = "1970-01-01 00:00:01") Timestamp rangeStart,
                                               @RequestParam(defaultValue = "2038-01-19 03:14:07") Timestamp rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") SortParams sort,
                                               @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) throws IOException, InterruptedException {
        log.info("Request received GET /events with: \n" +
                        "text = {},\n" +
                        "categories = {},\n" +
                        "paid = {},\n" +
                        "rangeStart = {},\n" +
                        "rangeEnd = {},\n" +
                        "onlyAvailable = {},\n" +
                        "sort = {},\n" +
                        "from = {},\n" +
                        "size = {}\n" +
                        "from ip = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());
        return EventMapper.toEventShortDtoList(
                service.getPublicEvents(text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        sort,
                        from,
                        size,
                        request.getRemoteAddr()));
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable Long id, HttpServletRequest request) throws IOException, InterruptedException {
        log.info("Request received GET /events/{} from ip = {}", id, request.getRemoteAddr());
        return EventMapper.toEventFullDto(service.getPublicEventById(id, request.getRemoteAddr()));
    }
}
