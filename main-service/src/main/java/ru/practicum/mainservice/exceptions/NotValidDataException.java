package ru.practicum.mainservice.exceptions;

import org.hibernate.JDBCException;

import java.sql.SQLException;

public class NotValidDataException extends JDBCException {
    public NotValidDataException(String message) {
        super(message, new SQLException());
    }
}
