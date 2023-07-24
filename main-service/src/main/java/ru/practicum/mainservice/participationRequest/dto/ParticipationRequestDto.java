package ru.practicum.mainservice.participationRequest.dto;

import lombok.Value;
import ru.practicum.mainservice.participationRequest.ParticipationRequestsStatus;

import java.sql.Timestamp;

@Value
public class ParticipationRequestDto {
    long id;
    long event;
    long requester;
    Timestamp created;
    ParticipationRequestsStatus status;
}
