package com.attest.ict.helper.ods.exception;

/**
 * Csv reader file exception.
 */
public class OdsReaderFileException extends RuntimeException {

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     */
    public OdsReaderFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public OdsReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
