package ru.practicum.mainservice.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.events.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation createCompilation(Compilation newCompilation) {
        if (newCompilation.getEventIds() != null) {
            updateCompilationEvents(newCompilation, newCompilation.getEventIds());
        }
        return compilationRepository.save(newCompilation);
    }

    @Override
    public void removeCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public Compilation updateCompilation(Compilation compilation) {
        Compilation newCompilation = compilationRepository
                .findById(compilation.getId())
                .orElseThrow(() -> new NotFoundException("Not found compilation with id = " + compilation.getId()));
        if (compilation.getTitle() != null) {
            newCompilation.setTitle(compilation.getTitle());
        }
        if (compilation.getPinned() != null) {
            newCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getEventIds() != null) {
            updateCompilationEvents(newCompilation, compilation.getEventIds());
        }
        return newCompilation;
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation getCompilationById(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Not found compilation with id = " + compId));
        if (compilation.getEventIdsString() != null && !compilation.getEventIdsString().isBlank()) {
            List<Long> eventIds = new ArrayList<>();
            Arrays.stream(compilation.getEventIdsString().split(",")).forEach(s -> eventIds.add(Long.valueOf(s)));
            updateCompilationEvents(compilation, eventIds);
        }
        return compilation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository
                .findAllByPinned(pinned, PageRequest.of(from / size, size))
                .getContent();
        Set<Long> eventIds = new HashSet<>();
        compilations.forEach(c -> {
            if (c.getEventIdsString() != null && !c.getEventIdsString().isBlank()) {
                Arrays.stream(c.getEventIdsString().split(",")).forEach(s -> eventIds.add(Long.valueOf(s)));
            }
        });
        List<Event> events = eventRepository.findAllByIdIn(new ArrayList<>(eventIds));
        if (events != null) {
            Map<Long, Event> eventsByIds = new HashMap<>();
            events.forEach(e -> eventsByIds.put(e.getId(), e));
            compilations.forEach(c -> {
                if (c.getEventIdsString() != null && !c.getEventIdsString().isBlank()) {
                    List<Long> compilationIds = new ArrayList<>();
                    Arrays.stream(c.getEventIdsString().split(",")).forEach(i -> compilationIds.add(Long.valueOf(i)));
                    c.setEventIds(compilationIds);
                    List<Event> compilationEvents = new ArrayList<>();
                    compilationIds.forEach(i -> compilationEvents.add(eventsByIds.get(i)));
                    c.setEvents(compilationEvents);
                }
            });
        }
        return compilations;
    }

    private void updateCompilationEvents(Compilation compilation, List<Long> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);
        StringJoiner ids = new StringJoiner(",");
        eventIds.forEach(i -> ids.add(i.toString()));
        compilation.setEvents(events);
        compilation.setEventIds(eventIds);
        compilation.setEventIdsString(ids.toString());
    }
}