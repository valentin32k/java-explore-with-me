package ru.practicum.mainservice.events.dto.input;

import lombok.Value;

import java.util.List;


@Value
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    EventRequestStatusUpdateRequestStatus status;
}
