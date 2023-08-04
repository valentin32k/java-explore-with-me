package ru.practicum.mainservice.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByCreatedBetween(
            Timestamp start,
            Timestamp end,
            PageRequest request);

    Page<Comment> findAllByEvent_IdInAndCreatedBetween(
            List<Long> eventIds,
            Timestamp start,
            Timestamp end,
            PageRequest request);

    Page<Comment> findAllByAuthor_IdInAndCreatedBetween(
            List<Long> authorIds,
            Timestamp start,
            Timestamp end,
            PageRequest request);

    Page<Comment> findAllByEvent_IdInAndAuthor_IdInAndCreatedBetween(
            List<Long> eventIds,
            List<Long> authorIds,
            Timestamp start,
            Timestamp end,
            PageRequest request);

    Page<Comment> findAllByEvent_IdIn(
            List<Long> eventIds,
            PageRequest request);

    Page<Comment> findAllByAuthor_IdIn(
            List<Long> authorIds,
            PageRequest request);

    Page<Comment> findAllByEvent_IdInAndAuthor_IdIn(
            List<Long> eventIds,
            List<Long> authorIds,
            PageRequest request);

    void deleteCommentByIdAndAuthorId(long commentId, long authorId);
}
