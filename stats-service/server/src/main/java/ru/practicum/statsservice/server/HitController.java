package ru.practicum.statsservice.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statsservice.dto.InputHitDto;
import ru.practicum.statsservice.dto.OutputHitDto;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService service;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createHit(@RequestBody @Valid InputHitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(hitDto.getTimestamp());
        log.info("Creating hit {}", service.createHit(hit));
        return "Информация сохранена";
    }

    @GetMapping("/stats")
    public List<OutputHitDto> getStats(@RequestParam(name = "start") Timestamp start,
                                       @RequestParam(name = "end") Timestamp end,
                                       @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                       @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Request receive GET /stats startTime = {}, endTime = {}, uris = {}, unique = {}",
                start,
                end,
                uris,
                unique);
        List<Hit> hits = service.getStats(start, end, uris, unique);
        if (hits == null) {
            return null;
        }
        return hits.stream()
                .map(h -> new OutputHitDto(
                        h.getApp(),
                        h.getUri(),
                        h.getHits()))
                .collect(Collectors.toList());
    }
}
