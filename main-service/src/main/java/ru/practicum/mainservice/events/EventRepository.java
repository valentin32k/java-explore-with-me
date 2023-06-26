package ru.practicum.mainservice.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(long initiatorId, PageRequest pageRequest);

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            Timestamp rangeStart,
            Timestamp rangeEnd,
            PageRequest pageRequest);

    @Query("select e from Event e where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category.id in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate between ?4 and ?5 ")
    Page<Event> searchPublicEvents(String text,
                                   List<Long> categories,
                                   Boolean paid,
                                   Timestamp rangeStart,
                                   Timestamp rangeEnd,
                                   PageRequest pageRequest);

    @Query("select e from Event e where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.paid = ?2 " +
            "and e.eventDate between ?3 and ?4 ")
    Page<Event> searchPublicEventsWithoutCategory(String text,
                                   Boolean paid,
                                   Timestamp rangeStart,
                                   Timestamp rangeEnd,
                                   PageRequest pageRequest);

    @Query("select e from Event e where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.category.id in ?2 " +
            "and e.eventDate between ?3 and ?4 ")
    Page<Event> searchPublicEventsWithouPaid(String text,
                                   List<Long> categories,
                                   Timestamp rangeStart,
                                   Timestamp rangeEnd,
                                   PageRequest pageRequest);

    @Query("select e from Event e where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "and e.eventDate between ?2 and ?3 ")
    Page<Event> searchPublicEventsWithouPaidAndCategory(String text,
                                             Timestamp rangeStart,
                                             Timestamp rangeEnd,
                                             PageRequest pageRequest);

    Event findEventByIdAndState(Long id, EventState state);
}
