package com.attest.ict.helper.excel.exception;

/**
 * Csv writer file exception.
 */
public class ExcelWriterFileException extends RuntimeException {

    /**
     * Instantiates a new Csv writer file exception.
     *
     * @param message the message
     */
    public ExcelWriterFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv writer file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ExcelWriterFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
