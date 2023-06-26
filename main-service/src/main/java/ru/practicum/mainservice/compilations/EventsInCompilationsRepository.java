package ru.practicum.mainservice.compilations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventsInCompilationsRepository extends JpaRepository<EventsInCompilations, Long> {

    List<EventsInCompilations> getEventsInCompilationsByCompilation_Id(long compilation_id);

    void  deleteAllByCompilation_Id(Long id);
}