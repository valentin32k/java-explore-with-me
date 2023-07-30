package ru.practicum.mainservice.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentMapper;
import ru.practicum.mainservice.comments.dto.NewCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/{adminId}/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final CommentService service;

    @GetMapping
    public List<CommentDto> getComments(@PathVariable long adminId,
                                        @RequestParam(defaultValue = "") List<Long> users,
                                        @RequestParam(defaultValue = "") List<Long> events,
                                        @RequestParam(defaultValue = "1970-01-01 00:00:01") Timestamp rangeStart,
                                        @RequestParam(defaultValue = "2038-01-19 03:14:07") Timestamp rangeEnd,
                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Request received GET /admin/{}/comments with:\n" +
                        "users = {},\n" +
                        "events = {},\n" +
                        "rangeStart = {},\n" +
                        "rangeEnd = {},\n" +
                        "from = {},\n" +
                        "size = {}",
                adminId, users, events, rangeStart, rangeEnd, from, size);
        return CommentMapper.toCommentDtoList(
                service.getComments(
                        adminId,
                        users,
                        events,
                        rangeStart,
                        rangeEnd,
                        from,
                        size));
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto newComment,
                                    @PathVariable long adminId,
                                    @PathVariable long commentId) {
        log.info("Request received PATCH /admin/{}/comments/{} " +
                "with text = {}, ", adminId, commentId, newComment.getText());
        return CommentMapper.toCommentDto(
                service.updateComment(
                        newComment.getText(),
                        adminId,
                        commentId,
                        true));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCommentById(@PathVariable long adminId,
                                  @PathVariable long commentId) {
        log.info("Request received DELETE /admin/{}/comments/{}", adminId, commentId);
        service.removeCommentById(
                adminId,
                commentId,
                true);
    }


}
