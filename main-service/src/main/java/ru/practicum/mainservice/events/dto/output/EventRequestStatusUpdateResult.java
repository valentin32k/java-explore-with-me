package ru.practicum.mainservice.events.dto.output;

import lombok.Data;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
