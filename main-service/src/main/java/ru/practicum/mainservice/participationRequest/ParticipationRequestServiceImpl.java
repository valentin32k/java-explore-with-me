package ru.practicum.mainservice.participationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.events.EventRepository;
import ru.practicum.mainservice.events.EventState;
import ru.practicum.mainservice.events.dto.input.EventRequestStatusUpdateRequestStatus;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.NotValidDataException;
import ru.practicum.mainservice.users.User;
import ru.practicum.mainservice.users.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequest createParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Can not find event with id = " + eventId));
        int eventParticipantCount = repository
                .findAllByEvent_IdAndStatus(
                        eventId,
                        ParticipationRequestsStatus.CONFIRMED)
                .size();
        if (userId == event.getInitiator().getId() ||
                EventState.PUBLISHED != event.getState() ||
                (eventParticipantCount >= event.getParticipantLimit() &&
                        event.getParticipantLimit() != 0 &&
                        !event.getRequestModeration())) {
            throw new NotValidDataException("Bad participation request data");
        }
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Can not find user with id = " + userId));
        ParticipationRequestsStatus status = ParticipationRequestsStatus.CONFIRMED;
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            status = ParticipationRequestsStatus.PENDING;
        }
        return repository.save(
                new ParticipationRequest(
                        0,
                        event,
                        user,
                        Timestamp.valueOf(LocalDateTime.now()),
                        status));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequest> getUserParticipationRequests(Long userId) {
        return repository.findAllByRequester_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequest> getEventParticipationRequests(Long userId, Long eventId) {
        List<ParticipationRequest> requests = repository.findAllByEvent_Id(eventId);
        if (requests != null &&
                requests.get(0).getEvent().getInitiator().getId() != userId) {
            throw new NotFoundException("Can not find requests of event with id = " + userId);
        }
        return requests;
    }

    @Override
    public ParticipationRequest cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = repository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Can not find requests with id = " + requestId));
        if (userId != request.getRequester().getId()) {
            throw new NotValidDataException("User with id = " + userId + " can not cancel request");
        }
        request.setStatus(ParticipationRequestsStatus.CANCELED);
        return request;
    }

    @Override
    public List<List<ParticipationRequest>> updateParticipationRequestStatus(Long userId,
                                                                             Long eventId,
                                                                             List<Long> requestIds,
                                                                             EventRequestStatusUpdateRequestStatus status) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Can not find event with id = " + eventId));
        if (repository
                .findAllByEvent_IdAndStatus(eventId, ParticipationRequestsStatus.CONFIRMED)
                .size() >= event.getParticipantLimit()) {
            throw new NotValidDataException("The limit of possible participants of the event has been exceeded");
        }
        if (status == EventRequestStatusUpdateRequestStatus.CONFIRMED) {
            repository.updateStatus(requestIds, ParticipationRequestsStatus.CONFIRMED);
        } else {
            repository.updateStatus(requestIds, ParticipationRequestsStatus.REJECTED);
        }
        List<List<ParticipationRequest>> response = new ArrayList<>();
        response.add(0, repository.findAllByEvent_IdAndStatus(eventId, ParticipationRequestsStatus.CONFIRMED));
        response.add(1, repository.findAllByEvent_IdAndStatus(eventId, ParticipationRequestsStatus.REJECTED));
        return response;
    }
}
