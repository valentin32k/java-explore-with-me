package ru.practicum.statsservice.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository repository;

    @Override
    @Transactional
    public Hit createHit(Hit hit) {
        return repository.save(hit);
    }

    @Override
    public List<Hit> getStats(Timestamp start, Timestamp end, List<String> uris, boolean unique) throws ValidationException {
        if (start.after(end)) {
            throw new ValidationException("Время начала и окончания выборки указаны не верно");
        }
        if (uris.isEmpty() && unique) {
            return repository.getAllUniqueUrisStats(start, end);
        }
        if (uris.isEmpty()) {
            return repository.getAllStats(start, end);
        }
        if (unique) {
            return repository.getUniqueUrisStats(start, end, uris);
        } else {
            return repository.getUrisStats(start, end, uris);
        }
    }
}
