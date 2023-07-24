package ru.practicum.mainservice.participationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.users.User;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@Entity
@Table(name = "participation_requests",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "requester_id"}, name = "dcsa")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotNull(message = "The event can not be null")
    @CollectionTable(name = "events", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @NotNull(message = "The requester can not be null")
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "requester_id")
    private User requester;

    @PastOrPresent
    private Timestamp created;

    @Enumerated(EnumType.STRING)
    private ParticipationRequestsStatus status;
}