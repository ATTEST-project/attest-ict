package com.attest.ict.helper.excel.exception;

/**
 * CSV invalid data exception.
 */
public class ExcelnvalidDataException extends RuntimeException {

    /**
     * Instantiates a new Csv invalid data exception.
     *
     * @param message the message
     */
    public ExcelnvalidDataException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv invalid data exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ExcelnvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
