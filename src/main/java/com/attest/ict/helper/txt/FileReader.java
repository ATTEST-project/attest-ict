package com.attest.ict.helper.txt;

import com.attest.ict.helper.txt.exception.ReaderFileException;
import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReader {

    public final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    /**
     * Search text specified as parameter in a file
     * @param filePath file to parse
     * @param searchString text to search
     * @return true if searchString is present in the file
     */
    public boolean searchString(String filePath, String searchString) {
        boolean isPresent = false;
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    LOGGER.info("The file: {}, contains the specified string: {}.", filePath, searchString);
                    isPresent = true;
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while reading the file: {}.", filePath, e.getMessage());
            throw new ReaderFileException("An error occurred while reading the file: " + filePath + e.getMessage());
        }
        return isPresent;
    }
}
