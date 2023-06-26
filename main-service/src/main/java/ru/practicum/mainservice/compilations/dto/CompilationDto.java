package ru.practicum.mainservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainservice.events.dto.output.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    private long id;
    private boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
