package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.categories.Category;
import ru.practicum.mainservice.categories.CategoryRepository;
import ru.practicum.mainservice.events.dto.input.AdminEventStateActions;
import ru.practicum.mainservice.events.dto.input.UserEventStateActions;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.NotValidDataException;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;
import ru.practicum.mainservice.participationRequest.ParticipationRequestRepository;
import ru.practicum.mainservice.participationRequest.ParticipationRequestsStatus;
import ru.practicum.mainservice.users.User;
import ru.practicum.mainservice.users.UserRepository;
import ru.practicum.statsservice.client.StatsClient;
import ru.practicum.statsservice.dto.InputHitDto;

import javax.validation.ValidationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public Event createEvent(Event event) {
        if (event.getEventDate().before(Timestamp.valueOf(LocalDateTime.now().plusHours(2L)))) {
            throw new NotValidDataException("Event date must be no earlier than 2 hours");
        }
        event.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        event.setState(EventState.PENDING);
        event.setInitiator(
                userRepository
                        .findById(event.getInitiatorId())
                        .orElseThrow(() -> new NotFoundException("Can not find user with id = " + event.getInitiatorId())));
        event.setCategory(
                categoryRepository
                        .findById(event.getCategoryId())
                        .orElseThrow(() -> new NotFoundException("Can not find category with id = " + event.getCategoryId())));
        return repository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getUserEvents(long userId, int from, int size) {
        return repository
                .findAllByInitiator_Id(userId, PageRequest.of(from / size, size))
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getUserEvent(long userId, long eventId) {
        Event event = repository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Can not find event with id = " + eventId));
        if (event.getInitiator().getId() != userId) {
            throw new NotValidDataException("Full information about the event can only be requested by the initiator of the event");
        }
        return event;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAdminEvents(List<Long> users,
                                      List<EventState> states,
                                      List<Long> categories,
                                      Timestamp rangeStart,
                                      Timestamp rangeEnd,
                                      Integer from,
                                      Integer size) {
        if (users.isEmpty()) {
            users = userRepository
                    .findAll()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        }
        if (states.isEmpty()) {
            states = List.of(
                    EventState.CANCELED,
                    EventState.PUBLISHED,
                    EventState.PENDING);
        }
        if (categories.isEmpty()) {
            categories = categoryRepository
                    .findAll()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }
        List<Event> events = repository
                .findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(
                        users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        PageRequest.of(from / size, size))
                .getContent();
        List<Long> eventIds = events
                .stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Long, List<ParticipationRequest>> req = participationRequestRepository
                .findAllByStatusAndEvent_IdIn(ParticipationRequestsStatus.CONFIRMED, eventIds)
                .stream()
                .collect(groupingBy(r -> r.getEvent().getId()));
        events.forEach(
                e -> {
                    if (req.get(e.getId()) != null) {
                        e.setConfirmedRequests(req.get(e.getId()).size());
                    }
                });
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getPublicEvents(String text,
                                       List<Long> categories,
                                       Boolean paid,
                                       Timestamp rangeStart,
                                       Timestamp rangeEnd,
                                       Boolean onlyAvailable,
                                       SortParams sort,
                                       Integer from,
                                       Integer size,
                                       String ip) throws IOException, InterruptedException {
        if (rangeStart.after(rangeEnd)) {
            throw new ValidationException("Not valid start and end ranges");
        }
        if (rangeStart.equals(Timestamp.valueOf("1970-01-01 00:00:01"))) {
            rangeStart = Timestamp.valueOf(LocalDateTime.now());
        }
        Sort sortParam = Sort.by(Sort.Direction.DESC, "views");
        if (sort == SortParams.EVENT_DATE) {
            sortParam = Sort.by(Sort.Direction.DESC, "eventDate");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size, sortParam);
        List<Event> events;
        if (paid == null && categories.size() != 0) {
            events = repository.searchPublicEventsWithouPaid(text, categories, rangeStart, rangeEnd, pageRequest).getContent();
        } else if (paid != null && categories.size() != 0) {
            events = repository.searchPublicEvents(text, categories, paid, rangeStart, rangeEnd, pageRequest).getContent();
        } else if (paid == null) {
            events = repository.searchPublicEventsWithouPaidAndCategory(text, rangeStart, rangeEnd, pageRequest).getContent();
        } else {
            events = repository.searchPublicEventsWithoutCategory(text, paid, rangeStart, rangeEnd, pageRequest).getContent();
        }
        writeStat("/events", ip);
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public Event getPublicEventById(Long id, String ip) throws IOException, InterruptedException {
        Event event = repository.findEventByIdAndState(id, EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Can not find event with id = " + id);
        }
        Long count = writeStat("/events/" + id, ip);
        event.setViews(count);
        return event;
    }

    @Override
    public Event updateUserEvent(Event newEvent, UserEventStateActions stateAction) {
        Event event = repository
                .findById(newEvent.getId())
                .orElseThrow(() -> new NotFoundException("Can not find event with id = " + newEvent.getId()));
        if ((newEvent.getEventDate() != null &&
                newEvent.getEventDate().before(Timestamp.valueOf(LocalDateTime.now().plusHours(2L)))) ||
                event.getState() == EventState.PUBLISHED) {
            throw new NotValidDataException("Not valid data");
        }
        updateEventValues(event, newEvent);
        if (stateAction == UserEventStateActions.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        } else {
            event.setState(EventState.CANCELED);
        }
        return event;
    }

    @Override
    public Event updateAdminEvent(Event newEvent, AdminEventStateActions stateActions) {
        Event event = repository
                .findById(newEvent.getId())
                .orElseThrow(() -> new NotFoundException("Can not find event with id = " + newEvent.getId()));
        if (EventState.PENDING != event.getState()) {
            throw new NotValidDataException("You can't change event with state = " + event.getState());
        }
        updateEventValues(event, newEvent);
        if (stateActions == AdminEventStateActions.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
        } else {
            event.setState(EventState.CANCELED);
        }
        return event;
    }

    private Long writeStat(String uri, String ip) throws IOException, InterruptedException {
        StatsClient client = new StatsClient("http://stats-service:9090");
        try {
            client.createHit(
                    new InputHitDto("ewm-main-service", uri, ip, Timestamp.valueOf(LocalDateTime.now())));
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return (long) client.getStats(
                        Timestamp.valueOf("1970-01-01 00:00:01"),
                        Timestamp.valueOf("2038-01-19 03:14:07"),
                        List.of(uri),
                        false)
                .size();
    }

    private void updateEventValues(Event event, Event newEvent) {
        if (newEvent.getAnnotation() != null) {
            event.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategoryId() != null) {
            event.setCategory(categoryRepository
                    .findById(newEvent.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Can not find category with id = " + newEvent.getCategoryId())));
        }
        if (newEvent.getDescription() != null) {
            event.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            event.setEventDate(newEvent.getEventDate());
        }
        if (newEvent.getLat() != null) {
            event.setLat(newEvent.getLat());
        }
        if (newEvent.getLon() != null) {
            event.setLon(newEvent.getLon());
        }
        if (newEvent.getPaid() != null) {
            event.setPaid(newEvent.getPaid());
        }
        if (newEvent.getParticipantLimit() != 0) {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getRequestModeration() != null) {
            event.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getTitle() != null) {
            event.setTitle(newEvent.getTitle());
        }
    }
}
