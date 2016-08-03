package com.jpos.desktopmode.ext.fw;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ApiNotVerifiedException extends Exception {
    public ApiNotVerifiedException() {
        super();
    }

    public ApiNotVerifiedException(String message) {
        super(message);
    }

    public ApiNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiNotVerifiedException(Throwable cause) {
        super(cause);
    }
}
