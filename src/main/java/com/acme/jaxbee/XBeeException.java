package com.acme.jaxbee;

/**
 * Created by pauleyj on 10/4/14.
 */
public class XBeeException extends Exception {
    public XBeeException() {
    }

    public XBeeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public XBeeException(Throwable cause) {
        super(cause);
    }

    public XBeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XBeeException(String message) {
        super(message);
    }
}
