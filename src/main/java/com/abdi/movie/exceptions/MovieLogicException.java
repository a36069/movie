package com.abdi.movie.exceptions;

public class MovieLogicException extends RuntimeException{
    public MovieLogicException() {
        super();
    }

    public MovieLogicException(String message) {
        super(message);
    }

    public MovieLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieLogicException(Throwable cause) {
        super(cause);
    }
}
