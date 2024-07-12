package org.example.exception;

public class RentalServiceException extends Exception{
    public RentalServiceException(String errorMessage) {
        super(errorMessage);
    }
}