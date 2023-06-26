package ru.practicum.mainservice.participationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateParticipationRequestController {
    private final ParticipationRequestService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        log.info("Request received POST /users/{}/requests?eventId={}", userId, eventId);
        return ParticipationRequestMapper.toParticipationRequestDto(
                service.createParticipationRequest(userId, eventId));
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserParticipationRequests(@PathVariable Long userId) {
        log.info("Request received GET /users/{}/requests", userId);
        return ParticipationRequestMapper.toParticipationRequestDtoList(
                service.getUserParticipationRequests(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {
        log.info("Request received PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return ParticipationRequestMapper.toParticipationRequestDto(
                service.cancelParticipationRequest(userId, requestId));
    }
}