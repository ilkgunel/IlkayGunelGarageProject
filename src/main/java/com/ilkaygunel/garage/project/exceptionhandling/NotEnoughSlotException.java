package com.ilkaygunel.garage.project.exceptionhandling;

public class NotEnoughSlotException extends Exception {
    public NotEnoughSlotException(String message) {
        super(message);
    }
}
