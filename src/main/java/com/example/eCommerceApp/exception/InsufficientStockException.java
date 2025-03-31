package com.example.eCommerceApp.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String notEnoughAvailable) {
        super(notEnoughAvailable);
    }
}
