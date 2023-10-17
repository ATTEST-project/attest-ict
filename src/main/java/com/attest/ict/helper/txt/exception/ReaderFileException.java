package com.attest.ict.helper.txt.exception;

/**
 * Reader file exception.
 */
public class ReaderFileException extends RuntimeException {

    /**
     * Instantiates a new  reader file exception.
     *
     * @param message the message
     */
    public ReaderFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new  reader file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
