package org.example.exception;

public class InvalidDurationException extends RentalServiceException {
    public InvalidDurationException(String errorMessage) {
        super(errorMessage);
    }
}