package com.ayush.jobboard.exceptions;

public class JobClosedException extends RuntimeException {
    public JobClosedException(String message) {
        super(message);
    }
}
