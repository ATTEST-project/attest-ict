package com.attest.ict.helper.matpower.exception;

/**
 * Matpower writer file exception.
 */
public class MatpowerWriterFileException extends RuntimeException {

    /**
     * Instantiates a new Matpower writer file exception.
     *
     * @param message the message
     */
    public MatpowerWriterFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Matpower writer file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public MatpowerWriterFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
