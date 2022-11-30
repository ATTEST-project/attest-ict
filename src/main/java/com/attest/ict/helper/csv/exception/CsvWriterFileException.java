package com.attest.ict.helper.csv.exception;

/**
 * Csv writer file exception.
 */
public class CsvWriterFileException extends RuntimeException {

    /**
     * Instantiates a new Csv writer file exception.
     *
     * @param message the message
     */
    public CsvWriterFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv writer file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CsvWriterFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
