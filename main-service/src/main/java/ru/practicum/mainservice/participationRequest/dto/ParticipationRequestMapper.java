package ru.practicum.mainservice.participationRequest.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ParticipationRequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest requests) {
        if (requests == null) {
            return null;
        }
        return new ParticipationRequestDto(
                requests.getId(),
                requests.getEvent().getId(),
                requests.getRequester().getId(),
                requests.getCreated(),
                requests.getStatus());
    }

    public List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
