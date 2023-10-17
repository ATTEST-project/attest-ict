package com.attest.ict.helper.txt;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class TxtFileReaderTest {

    private String stringToSearch = "LoadError: No violations. This case is a waste of time";

    @Test
    void testSearchStringPresent() {
        String filePath = "src\\test\\resources\\txt_file\\log_with_error.txt";
        FileReader reader = new FileReader();
        boolean isPresent = reader.searchString(filePath, stringToSearch);
        System.out.println(" FilePath: " + filePath + " , - Search string: " + stringToSearch + " , isPresent: " + isPresent);
        Assert.isTrue(isPresent);
    }

    @Test
    void searchStringNotPresent() {
        String filePath = "src\\test\\resources\\txt_file\\log_without_error.txt";
        FileReader reader = new FileReader();
        boolean isPresent = reader.searchString(filePath, stringToSearch);
        System.out.println(" FilePath: " + filePath + " , - Search string: " + stringToSearch + " , isPresent: " + isPresent);
        Assert.isTrue(!isPresent);
    }
}
