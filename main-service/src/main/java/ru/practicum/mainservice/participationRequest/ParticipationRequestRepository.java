package ru.practicum.mainservice.participationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_Id(Long requestorId);

    @Modifying
    @Query("update ParticipationRequest p set p.status = ?2 where p.id in ?1")
    void updateStatus(List<Long> ids, ParticipationRequestsStatus status);

    List<ParticipationRequest> findAllByEvent_IdAndStatus(Long eventId, ParticipationRequestsStatus status);

    List<ParticipationRequest> findAllByStatusAndEvent_IdIn(ParticipationRequestsStatus status, List<Long> eventIds);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);
}