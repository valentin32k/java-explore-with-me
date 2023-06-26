package ru.practicum.statsservice.server;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.List;

public interface HitService {
    Hit createHit(Hit hit);

    List<Hit> getStats(Timestamp start, Timestamp end, List<String> uris, boolean unique) throws ValidationException;
}
