package ru.gil.exception;

public class NotElementHeapException extends RuntimeException{
    public NotElementHeapException() {
    }

    public NotElementHeapException(String message) {
        super(message);
    }

    public NotElementHeapException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotElementHeapException(Throwable cause) {
        super(cause);
    }

    public NotElementHeapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
