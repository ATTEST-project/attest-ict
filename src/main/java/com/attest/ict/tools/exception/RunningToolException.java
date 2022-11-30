package com.attest.ict.tools.exception;

public class RunningToolException extends RuntimeException {

    /**
     * Instantiates a new RunningToolException invalid data exception.
     *
     * @param message the message
     */
    public RunningToolException(String message) {
        super(message);
    }

    /**
     * Instantiates a new RunningToolException invalid data exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RunningToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
