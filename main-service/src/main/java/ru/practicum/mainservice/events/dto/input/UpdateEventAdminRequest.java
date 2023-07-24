package ru.practicum.mainservice.events.dto.input;
import lombok.Data;

@Data
public class UpdateEventAdminRequest extends CommonUpdateEventDto {
    private AdminEventStateActions stateAction;
}
