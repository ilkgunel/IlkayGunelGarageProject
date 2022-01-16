package com.ilkaygunel.garage.project.exceptionhandling;

public class NoLeftSlotException extends Exception {
    public NoLeftSlotException(String message) {
        super(message);
    }
}
