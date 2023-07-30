package ru.practicum.mainservice.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.comments.Comment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        if (comment.getUpdater() != null && comment.getUpdated() != null) {
            commentDto.setUpdaterName(comment.getUpdater().getName());
            commentDto.setUpdated(comment.getUpdated().toLocalDateTime());
        }
        return commentDto;
    }

    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
