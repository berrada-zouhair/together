package com.together.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class UserNotExistingException extends RuntimeException {

    public UserNotExistingException(Long userId) {
        super(format("No existing user with id '%d'", userId));
    }
}
