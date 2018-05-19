package io.domino.lab.reflection.exception;

/**
 * Created by amazimpaka on 2018-05-03
 */
public class ReflectionException extends RuntimeException{

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
