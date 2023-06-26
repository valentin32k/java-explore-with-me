package ru.practicum.mainservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.mainservice.categories.Category;
import ru.practicum.mainservice.users.User;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The field annotation can not be blank")
    @Size(min = 20, max = 2000, message = "Annotation must be longer then 20 and shorter then 2000 characters")
    private String annotation;

    @ManyToOne
    @NotNull(message = "Event must have category")
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private long confirmedRequests;

    @NotNull(message = "The field createOn can not be null")
    @Column(name = "create_on")
    @PastOrPresent
    private Timestamp createOn;

    @NotBlank(message = "The field description can not be blank")
    @Size(min = 20, max = 7000, message = "Description must be longer then 20 and shorter then 7000 characters")
    private String description;

    @NotNull(message = "The field eventDate can not be null")
    @Column(name = "event_date")
    @Future
    private Timestamp eventDate;

    @ManyToOne
    @NotNull(message = "Event must have initiator")
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "initiator_id")
    private User initiator;

    private float lat;

    private float lon;

    @NotNull(message = "Available cannot be null")
    @Column(name = "is_paid", nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit = 0;

    @NotNull(message = "The field publishedOn can not be null")
    @Column(name = "published_on")
    @PastOrPresent
    private Timestamp publishedOn;

    @NotNull(message = "requestModeration cannot be null")
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The field state can not be null")
    private EventState state;

    @NotBlank(message = "The field title can not be blank")
    @Size(min = 3, max = 120, message = "Title must be longer then 3 and shorter then 120 characters")
    private String title;

    private long views;

    @Transient
    long categoryId;
    @Transient
    long initiatorId;
}
