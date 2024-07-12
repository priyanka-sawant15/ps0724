package org.example.exception;

public class InvalidDiscountException extends RentalServiceException{
    public InvalidDiscountException(String errorMessage) {
        super(errorMessage);
    }
}

