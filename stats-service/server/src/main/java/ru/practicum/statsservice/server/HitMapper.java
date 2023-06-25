package ru.practicum.statsservice.server;

import lombok.experimental.UtilityClass;
import ru.practicum.statsservice.dto.InputHitDto;
import ru.practicum.statsservice.dto.OutputHitDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class HitMapper {
    public Hit fromInputHitDto(InputHitDto inputHitDto) {
        return new Hit(0,
                inputHitDto.getApp(),
                inputHitDto.getUri(),
                inputHitDto.getIp(),
                inputHitDto.getTimestamp(),
                0);
    }

    public OutputHitDto toOutputHitDto(Hit hit) {
        if (hit == null) {
            return null;
        }
        return new OutputHitDto(hit.getApp(),
                hit.getUri(),
                hit.getHits());
    }

    public List<OutputHitDto> toOutputHitDtoList(List<Hit> hits) {
        if (hits == null) {
            return null;
        }
        return hits.stream()
                .map(HitMapper::toOutputHitDto)
                .collect(Collectors.toList());
    }
}