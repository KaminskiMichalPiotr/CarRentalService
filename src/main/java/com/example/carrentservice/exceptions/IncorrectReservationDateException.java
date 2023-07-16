package com.example.carrentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectReservationDateException extends RuntimeException {


    public IncorrectReservationDateException(String message) {
        super(message);
    }


}
