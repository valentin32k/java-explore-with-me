package ru.practicum.mainservice.events.dto.input;

import lombok.Data;

@Data
public class UpdateEventUserRequest extends CommonUpdateEventDto {
    private UserEventStateActions stateAction;
}
