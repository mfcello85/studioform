package it.studioform.test.exceptions;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
    private final String message;
}
