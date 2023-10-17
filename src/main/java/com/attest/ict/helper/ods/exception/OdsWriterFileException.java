package com.attest.ict.helper.ods.exception;

/**
 * Ods writer file exception.
 */
public class OdsWriterFileException extends RuntimeException {

    /**
     * Instantiates a new Ods writer file exception.
     *
     * @param message the message
     */
    public OdsWriterFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Ods writer file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public OdsWriterFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
