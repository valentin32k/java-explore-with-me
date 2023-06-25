package ru.practicum.statsservice.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.statsservice.server.Hit(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.uri " +
            "order by count (*) desc")
    List<Hit> getUrisStats(Timestamp start, Timestamp end, List<String> uris);

    @Query("select new ru.practicum.statsservice.server.Hit(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.uri " +
            "order by count (*) desc")
    List<Hit> getAllStats(Timestamp start, Timestamp end);

    @Query("select new ru.practicum.statsservice.server.Hit(h.app, h.uri, count(distinct h.uri)) " +
            "from Hit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.uri " +
            "order by count (distinct h.uri) desc")
    List<Hit> getUniqueUrisStats(Timestamp start, Timestamp end, List<String> uris);

    @Query("select new ru.practicum.statsservice.server.Hit(h.app, h.uri, count(distinct h.uri)) " +
            "from Hit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.uri " +
            "order by count (distinct h.uri) desc")
    List<Hit> getAllUniqueUrisStats(Timestamp start, Timestamp end);
}
