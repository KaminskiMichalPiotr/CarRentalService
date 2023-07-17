package com.example.carrentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectEntityException extends RuntimeException {

    public IncorrectEntityException(String message) {
        super(message);
    }

}
