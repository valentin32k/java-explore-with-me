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
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping(path = "/users/events/{eventId}/comment")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newComment,
                                    @RequestHeader("X-Explorer-Requestor-Id") long userId,
                                    @PathVariable long eventId) {
        log.info("Request received POST users/events/{}/comment " +
                "with text = {}, and Header \"X-Explorer-Requestor-Id\" = {}", eventId, newComment.getText(), userId);
        return CommentMapper.toCommentDto(service.createComment(newComment.getText(), userId, eventId));
    }

    @GetMapping
    public List<CommentDto> getCommentsByUserId(@RequestHeader("X-Explorer-Requestor-Id") long userId,
                                                @PathVariable long eventId,
                                                @RequestParam(required = false) Timestamp rangeStart,
                                                @RequestParam(required = false) Timestamp rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET users/events/{}/comment/user?rangeStart={}&rangeEnd={}&from={}&size={}\n" +
                        "with Header \"X-Explorer-Requestor-Id\" = {}", eventId, rangeStart, rangeEnd, from, size,
                userId);
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
                                    @RequestHeader("X-Explorer-Requestor-Id") long userId,
                                    @PathVariable long eventId,
                                    @PathVariable long commentId) {
        log.info("Request received PATCH users/events/{}/comment/{} with text = {}, \n" +
                        "with Header \"X-Explorer-Requestor-Id\" = {}",
                eventId, commentId, newComment.getText(), userId);
        return CommentMapper.toCommentDto(
                service.updateComment(
                        newComment.getText(),
                        userId,
                        commentId,
                        false));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCommentById(@RequestHeader("X-Explorer-Requestor-Id") long userId,
                                  @PathVariable long eventId,
                                  @PathVariable long commentId) {
        log.info("Request received DELETE users/events/{}/comment/{} \n" +
                        "with Header \"X-Explorer-Requestor-Id\"={}",
                eventId, commentId, userId);
        service.removeCommentById(userId, commentId, false);
    }
}
