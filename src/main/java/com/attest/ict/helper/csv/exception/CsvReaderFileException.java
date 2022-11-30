package com.attest.ict.helper.csv.exception;

/**
 * Csv reader file exception.
 */
public class CsvReaderFileException extends RuntimeException {

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     */
    public CsvReaderFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CsvReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
