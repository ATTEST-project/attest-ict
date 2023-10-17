package com.attest.ict.helper.matpower.exception;

/**
 * Matpower file's reader file exception.
 */
public class MatpowerReaderFileException extends RuntimeException {

    /**
     * Instantiates a new matpower reader file exception.
     *
     * @param message the message
     */
    public MatpowerReaderFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new matpower reader file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public MatpowerReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
