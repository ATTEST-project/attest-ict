package com.attest.ict.helper.csv.exception;

/**
 * CSV invalid data exception.
 */
public class CsvInvalidDataException extends RuntimeException {

    /**
     * Instantiates a new Csv invalid data exception.
     *
     * @param message the message
     */
    public CsvInvalidDataException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv invalid data exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CsvInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
