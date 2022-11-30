package com.attest.ict.custom.exception;

/**
 * Csv writer file exception.
 */
public class ImportToolCsvFileException extends RuntimeException {

    /**
     * Instantiates a new ImportToolCsvFileException exception.
     *
     * @param message the message
     */
    public ImportToolCsvFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new ImportToolCsvFileException exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ImportToolCsvFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
