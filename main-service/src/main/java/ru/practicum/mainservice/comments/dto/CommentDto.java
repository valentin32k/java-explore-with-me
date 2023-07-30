package ru.practicum.mainservice.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    long id;
    String text;
    String authorName;
    LocalDateTime created;
    String updaterName;
    LocalDateTime updated;
}
