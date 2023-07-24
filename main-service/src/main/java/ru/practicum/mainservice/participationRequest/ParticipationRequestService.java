package ru.practicum.mainservice.participationRequest;

import ru.practicum.mainservice.events.dto.input.EventRequestStatusUpdateRequestStatus;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequest createParticipationRequest(Long userId, Long eventId);

    ParticipationRequest cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequest> getUserParticipationRequests(Long userId);

    List<ParticipationRequest> getEventParticipationRequests(Long userId, Long eventId);

    List<List<ParticipationRequest>> updateParticipationRequestStatus(Long userId,
                                                                      Long eventId,
                                                                      List<Long> requestIds,
                                                                      EventRequestStatusUpdateRequestStatus status);
}
