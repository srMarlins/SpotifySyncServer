package com.andrewkingmarshall.Exceptions;

/**
 * Created by jfowler on 3/31/16.
 */
public class PlaylistNotFoundException extends Exception {
    public PlaylistNotFoundException() {
        super();
    }

    public PlaylistNotFoundException(String message) {
        super(message);
    }

    public PlaylistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaylistNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PlaylistNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
