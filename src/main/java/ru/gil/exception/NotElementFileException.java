package ru.gil.exception;

public class NotElementFileException extends RuntimeException{
    public NotElementFileException() {
    }

    public NotElementFileException(String message) {
        super(message);
    }

    public NotElementFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotElementFileException(Throwable cause) {
        super(cause);
    }

    public NotElementFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
