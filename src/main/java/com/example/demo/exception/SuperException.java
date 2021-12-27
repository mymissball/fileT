package com.example.demo.exception;

public class SuperException extends RuntimeException {
    public SuperException() {
        super();
    }

    public SuperException(String message) {
        super(message);
    }

    public SuperException(String message, Throwable cause) {
        super(message, cause);
    }

    public SuperException(Throwable cause) {
        super(cause);
    }

    protected SuperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
