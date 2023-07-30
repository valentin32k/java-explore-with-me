package ru.practicum.mainservice.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentMapper;
import ru.practicum.mainservice.comments.dto.NewCommentDto;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comment")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newComment,
                                    @PathVariable long userId,
                                    @PathVariable long eventId) {
        log.info("Request received POST users/{}/events/{}/comment " +
                "with text = {}, ", userId, eventId, newComment.getText());
        return CommentMapper.toCommentDto(service.createComment(newComment.getText(), userId, eventId));
    }

    @GetMapping("/event")
    public List<CommentDto> getCommentsByEventId(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @RequestParam(defaultValue = "1970-01-01 00:00:01") Timestamp rangeStart,
                                                 @RequestParam(defaultValue = "2038-01-19 03:14:07") Timestamp rangeEnd,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET users/{}/events/{}/comment/event?rangeStart={}&rangeEnd={}&from={}&size={}",
                userId, eventId, rangeStart, rangeEnd, from, size);
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

    @GetMapping("/user")
    public List<CommentDto> getCommentsByUserId(@PathVariable long userId,
                                                @PathVariable long eventId,
                                                @RequestParam(defaultValue = "1970-01-01 00:00:01") Timestamp rangeStart,
                                                @RequestParam(defaultValue = "2038-01-19 03:14:07") Timestamp rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET users/{}/events/{}/comment/user?rangeStart={}&rangeEnd={}&from={}&size={}",
                userId, eventId, rangeStart, rangeEnd, from, size);
        return CommentMapper.toCommentDtoList(
                service.getComments(
                        0L,
                        List.of(userId),
                        Collections.emptyList(),
                        rangeStart,
                        rangeEnd,
                        from,
                        size));
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto newComment,
                                    @PathVariable long userId,
                                    @PathVariable long eventId,
                                    @PathVariable long commentId) {
        log.info("Request received PATCH users/{}/events/{}/comment/{} with text = {}, ",
                userId, eventId, commentId, newComment.getText());
        return CommentMapper.toCommentDto(
                service.updateComment(
                        newComment.getText(),
                        userId,
                        commentId,
                        false));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCommentById(@PathVariable long userId,
                                  @PathVariable long eventId,
                                  @PathVariable long commentId) {
        log.info("Request received DELETE users/{}/events/{}/comment/{}",
                userId, eventId, commentId);
        service.removeCommentById(userId, commentId, false);
    }
}
