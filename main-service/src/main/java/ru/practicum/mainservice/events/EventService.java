package ru.practicum.mainservice.events;

import ru.practicum.mainservice.events.dto.input.AdminEventStateActions;
import ru.practicum.mainservice.events.dto.input.UserEventStateActions;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public interface EventService {
    Event createEvent(Event event);

    List<Event> getUserEvents(long userId, int from, int size);

    Event getUserEvent(long userId, long eventId);

    Event updateUserEvent(Event event, UserEventStateActions action);

    Event updateAdminEvent(Event event, AdminEventStateActions stateActions);

    List<Event> getAdminEvents(List<Long> users,
                               List<EventState> states,
                               List<Long> categories,
                               Timestamp rangeStart,
                               Timestamp rangeEnd,
                               Integer from,
                               Integer size);

    List<Event> getPublicEvents(String text,
                                List<Long> categories,
                                Boolean paid,
                                Timestamp rangeStart,
                                Timestamp rangeEnd,
                                Boolean onlyAvailable,
                                SortParams sort,
                                Integer from,
                                Integer size,
                                String ip) throws IOException, InterruptedException;

    Event getPublicEventById(Long id, String ip) throws IOException, InterruptedException;
}
