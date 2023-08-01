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
        commentDto.setCreated(comment.getCreated().toLocalDateTime());
        if (comment.getLastUpdatedAt() != null && comment.getLastUpdatedBy() != null) {
            commentDto.setLastUpdatedBy(comment.getLastUpdatedBy().getName());
            commentDto.setLastUpdatedAt(comment.getLastUpdatedAt().toLocalDateTime());
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
