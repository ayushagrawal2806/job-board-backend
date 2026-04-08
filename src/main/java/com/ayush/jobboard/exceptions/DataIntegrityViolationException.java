package com.ayush.jobboard.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String message){
        super(message);
    }
}
