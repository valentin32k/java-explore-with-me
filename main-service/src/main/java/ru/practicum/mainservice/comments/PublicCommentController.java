package ru.practicum.mainservice.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentMapper;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/comment")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    private final CommentService service;

    @GetMapping
    public List<CommentDto> getCommentsByEventId(@PathVariable long eventId,
                                                 @RequestParam(required = false) Timestamp rangeStart,
                                                 @RequestParam(required = false) Timestamp rangeEnd,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET /events/{}/comment?rangeStart={}&rangeEnd={}&from={}&size={}",
                eventId, rangeStart, rangeEnd, from, size);
        return CommentMapper.toCommentDtoList(
                service.getComments(
                        0L,
                        Collections.emptyList(),
                        List.of(eventId),
                        rangeStart,
                        rangeEnd,
                        from,
                        size));
    }
}
