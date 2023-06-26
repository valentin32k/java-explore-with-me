package ru.practicum.mainservice.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.events.Event;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "events_in_compilations", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventsInCompilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @CollectionTable(name = "events", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @CollectionTable(name = "compilations", joinColumns = @JoinColumn(name = "id"))
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;
}
