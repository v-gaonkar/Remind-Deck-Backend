package com.dev.remindeck.exception;

public class DeckAlreadyExistsException extends RuntimeException {
    public DeckAlreadyExistsException(String message) {
        super(message);
    }
}
