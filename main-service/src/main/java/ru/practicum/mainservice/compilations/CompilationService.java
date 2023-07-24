package ru.practicum.mainservice.compilations;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(Compilation newCompilation);

    void removeCompilation(Long compId);

    Compilation updateCompilation(Compilation compilation);

    Compilation getCompilationById(Long compId);

    List<Compilation> getCompilations(Boolean pinned, int from, int size);
}