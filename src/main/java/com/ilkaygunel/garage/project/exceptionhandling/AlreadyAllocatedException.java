package com.ilkaygunel.garage.project.exceptionhandling;

public class AlreadyAllocatedException extends Exception {
    public AlreadyAllocatedException(String message) {
        super(message);
    }
}
