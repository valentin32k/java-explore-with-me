package ru.practicum.mainservice.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.users.User;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The field comment text cannot be blank")
    @Size(min = 1, max = 4095, message = "Comment text must be longer then 1 and shorter then 4095 characters")
    private String text;

    @ManyToOne
    @CollectionTable(name = "events", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @NotNull(message = "The field author can not be null")
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "author_id")
    private User author;

    @NotNull(message = "The field created can not be null")
    @PastOrPresent
    private Timestamp created;

    @ManyToOne
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @PastOrPresent
    private Timestamp lastUpdatedAt;
}
