package com.attest.ict.custom.exception;

public class KillProcessException extends Exception {

    public KillProcessException(String message) {
        super(message);
    }

    public KillProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
