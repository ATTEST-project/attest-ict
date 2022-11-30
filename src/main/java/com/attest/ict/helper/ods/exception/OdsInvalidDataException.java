package com.attest.ict.helper.ods.exception;

/**
 * ODS invalid data exception.
 */
public class OdsInvalidDataException extends RuntimeException {

    /**
     * Instantiates a new Ods invalid data exception.
     *
     * @param message the message
     */
    public OdsInvalidDataException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Ods invalid data exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public OdsInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
