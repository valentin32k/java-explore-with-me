package ru.practicum.mainservice.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.compilations.Compilation;
import ru.practicum.mainservice.events.dto.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .eventIds(newCompilationDto.getEvents())
                .build();
    }

    public Compilation fromUpdateCompilationRequest(UpdateCompilationRequest updateCompilationRequest) {
        return Compilation.builder()
                .title(updateCompilationRequest.getTitle())
                .pinned(updateCompilationRequest.getPinned())
                .eventIds(updateCompilationRequest.getEvents())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                EventMapper.toEventShortDtoList(compilation.getEvents()));
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        if (compilations == null) {
            return null;
        }
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
