package com.jpos.desktopmode.ext.fw;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/6/2016.
 */

@SuppressWarnings("WeakerAccess")
public class UserIdInvalidException extends RuntimeException {
    public UserIdInvalidException() {
        super();
    }

    public UserIdInvalidException(String message) {
        super(message);
    }

    public UserIdInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserIdInvalidException(Throwable cause) {
        super(cause);
    }
}
