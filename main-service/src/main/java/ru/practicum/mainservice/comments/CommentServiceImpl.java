package ru.practicum.mainservice.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.events.EventRepository;
import ru.practicum.mainservice.events.EventState;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.NotValidDataException;
import ru.practicum.mainservice.users.User;
import ru.practicum.mainservice.users.UserRepository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(String commentText, long userId, long eventId) {
        Event event = eventRepository
                .findById(eventId).get();
        User user = userRepository
                .findById(userId).get();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotValidDataException("You can't comment on an event before it's published");
        }
        return repository.save(
                new Comment(
                        0,
                        commentText,
                        event,
                        user,
                        Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC())),
                        null,
                        null));
    }

    @Override
    public List<Comment> getComments(long adminId,
                                     List<Long> users,
                                     List<Long> events,
                                     Timestamp rangeStart,
                                     Timestamp rangeEnd,
                                     Integer from,
                                     Integer size) {
        List<Comment> comments;
        PageRequest request = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        if (users.isEmpty() && events.isEmpty() && rangeStart != null && rangeEnd != null) {
            comments = repository.findAllByCreatedBetween(rangeStart, rangeEnd, request).getContent();
        } else if (users.isEmpty() && rangeStart != null && rangeEnd != null) {
            comments = repository.findAllByEvent_IdInAndCreatedBetween(events, rangeStart, rangeEnd, request).getContent();
        } else if (events.isEmpty() && rangeStart != null && rangeEnd != null) {
            comments = repository.findAllByAuthor_IdInAndCreatedBetween(users, rangeStart, rangeEnd, request).getContent();
        } else if (rangeStart != null && rangeEnd != null) {
            comments = repository.findAllByEvent_IdInAndAuthor_IdInAndCreatedBetween(events, users, rangeStart, rangeEnd, request).getContent();
        } else if (users.isEmpty() && events.isEmpty()) {
            comments = repository.findAll(request).getContent();
        } else if (users.isEmpty()) {
            comments = repository.findAllByEvent_IdIn(events, request).getContent();
        } else if (events.isEmpty()) {
            comments = repository.findAllByAuthor_IdIn(users, request).getContent();
        } else {
            comments = repository.findAllByEvent_IdInAndAuthor_IdIn(events, users, request).getContent();
        }
        return comments;
    }

    @Override
    public Comment updateComment(String commentText, long userId, long commentId, boolean isUpdatedByAdmin) {
        Comment updatedComment = repository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundException("Can not find comment with id = " + commentId));
        User updater = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Can not find user with id = " + userId));
        if (updatedComment.getAuthor().getId() != userId && !isUpdatedByAdmin) {
            throw new NotValidDataException("Only comment author or admin can updated the comment");
        }
        updatedComment.setText(commentText);
        updatedComment.setLastUpdatedBy(updater);
        updatedComment.setLastUpdatedAt(Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC())));
        return updatedComment;
    }

    @Override
    public void removeCommentById(long userId, long commentId, boolean isRemovedByAdmin) {
        if (isRemovedByAdmin) {
            repository.deleteById(commentId);
        } else {
            repository.deleteCommentByIdAndAuthorId(commentId, userId);
        }
    }
}