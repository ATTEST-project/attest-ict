package com.attest.ict.helper.excel.exception;

/**
 * Csv reader file exception.
 */
public class ExcelReaderFileException extends RuntimeException {

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     */
    public ExcelReaderFileException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Csv reader file exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ExcelReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
