package ru.practicum.mainservice.comments;

import java.sql.Timestamp;
import java.util.List;

public interface CommentService {
    Comment createComment(String commentText, long userId, long eventId);

    Comment updateComment(String commentText, long userId, long commentId, boolean isUpdatedByAdmin);

    void removeCommentById(long userId, long commentId, boolean isRemovedByAdmin);

    List<Comment> getComments(long adminId,
                              List<Long> users,
                              List<Long> events,
                              Timestamp rangeStart,
                              Timestamp rangeEnd,
                              Integer from,
                              Integer size);
}
