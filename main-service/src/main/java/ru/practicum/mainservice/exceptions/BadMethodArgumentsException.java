package ru.practicum.mainservice.exceptions;

public class BadMethodArgumentsException extends RuntimeException {
    public BadMethodArgumentsException(String message) {
        super(message);
    }
}
