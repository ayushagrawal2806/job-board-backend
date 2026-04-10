package com.ayush.jobboard.exceptions;

public class AlreadySavedJobException extends RuntimeException{

    public AlreadySavedJobException(String message){
        super(message);
    }
}
