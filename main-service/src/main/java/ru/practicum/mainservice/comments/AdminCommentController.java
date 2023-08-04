package ru.practicum.mainservice.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentMapper;
import ru.practicum.mainservice.comments.dto.NewCommentDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final CommentService service;

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto newComment,
                                    @RequestHeader("X-Explorer-Requestor-Id") long adminId,
                                    @PathVariable long commentId) {
        log.info("Request received PATCH /admin/comments/{} " +
                "with text = {}, and \n" +
                "Header \"X-Explorer-Requestor-Id\" = {}", commentId, newComment.getText(), adminId);
        return CommentMapper.toCommentDto(
                service.updateComment(
                        newComment.getText(),
                        adminId,
                        commentId,
                        true));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCommentById(@RequestHeader("X-Explorer-Requestor-Id") long adminId,
                                  @PathVariable long commentId) {
        log.info("Request received DELETE /admin/comments/{} \n" +
                "with Header \"X-Explorer-Requestor-Id\"  = {}", commentId, adminId);
        service.removeCommentById(
                adminId,
                commentId,
                true);
    }
}
