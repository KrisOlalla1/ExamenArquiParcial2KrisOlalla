package com.examen.branches_api.exception;

public class HolidayNotFoundException extends RuntimeException {
    
    public HolidayNotFoundException(String message) {
        super(message);
    }
}
