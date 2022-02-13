package com.amin.redis.api.exception;

public class ApiException extends Exception{
    public ApiException(String errorMessage) {
        super(errorMessage);
    }
}
