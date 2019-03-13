package com.together.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class EventNotExistingException extends RuntimeException {

    public EventNotExistingException(Long eventId) {
        super(format("No existing event with id '%d'", eventId));
    }
}
