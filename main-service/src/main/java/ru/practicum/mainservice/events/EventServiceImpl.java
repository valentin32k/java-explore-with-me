package ru.practicum.mainservice.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.mainservice.exceptions.BadMethodArgumentsException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public String createEvent(Event event) {
        if(event.getEventDate().before(Timestamp.valueOf(LocalDateTime.now().plusHours(2L)))) {
            throw new MethodArgumentNotValidException("Запрос составлен некорректно");
        }
        repository.save(event);
        return ;
    }
}
