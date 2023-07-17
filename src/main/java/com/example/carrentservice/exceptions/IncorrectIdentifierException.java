package com.example.carrentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IncorrectIdentifierException extends RuntimeException{

    public IncorrectIdentifierException(String message) {
        super(message);
    }

}
