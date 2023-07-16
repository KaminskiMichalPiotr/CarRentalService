package com.example.carrentservice.exceptions;

import java.util.List;

public record ErrorResponse(String message, List<String> description) {

}
