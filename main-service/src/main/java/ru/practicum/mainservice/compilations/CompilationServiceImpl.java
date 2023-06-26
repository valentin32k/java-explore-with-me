package ru.practicum.mainservice.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.events.Event;
import ru.practicum.mainservice.events.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsInCompilationsRepository eventsInCompilationsRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation createCompilation(Compilation newCompilation) {
        newCompilation = compilationRepository.save(newCompilation);
        if (newCompilation.getEventIds() == null) {
            return getCompilationById(newCompilation.getId());
        }
        Compilation finalNewCompilation = newCompilation;
        newCompilation.getEventIds().forEach(eid ->
                eventsInCompilationsRepository.save(
                        new EventsInCompilations(0,
                                eventRepository
                                        .findById(eid)
                                        .orElseThrow(() -> new NotFoundException("Not found event with id = " + eid)),
                                finalNewCompilation)));
        return getCompilationById(newCompilation.getId());
    }

    @Override
    public void removeCompilation(Long compId) {
        compilationRepository.deleteById(compId);
        eventsInCompilationsRepository.deleteAllByCompilation_Id(compId);
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
            eventsInCompilationsRepository.deleteAllByCompilation_Id(compilation.getId());
            compilation.getEventIds().forEach(eid ->
                    eventsInCompilationsRepository.save(
                            new EventsInCompilations(0,
                                    eventRepository
                                            .findById(eid)
                                            .orElseThrow(() -> new NotFoundException("Not found event with id = " + eid)),
                                    newCompilation)));
        }
        return newCompilation;
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation getCompilationById(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Not found compilation with id = " + compId));
        compilation.setEvents(getCompilationEvents(compId));
        return compilation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository
                .findAllByPinned(pinned, PageRequest.of(from / size, size))
                .getContent();
        compilations.forEach(
                c -> c.setEvents(this.getCompilationEvents(c.getId())));
        return compilations;
    }

    private List<Event> getCompilationEvents(Long compilationId) {
        return eventsInCompilationsRepository
                .getEventsInCompilationsByCompilation_Id(compilationId)
                .stream()
                .map(EventsInCompilations::getEvent)
                .collect(Collectors.toList());
    }
}
