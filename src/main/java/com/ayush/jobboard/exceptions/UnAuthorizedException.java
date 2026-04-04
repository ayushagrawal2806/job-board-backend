package com.ayush.jobboard.exceptions;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message){
        super(message);
    }
}
