package com.attest.ict.helper.excel.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ExcelFileReaderTest {

    private String filePath = "src\\test\\resources\\excel_file\\Load\\Network1_A_Bd.xlsx";
    private String filePathCsv = "src\\test\\resources\\csv_file\\test_15m.csv";

    private boolean headerEnabled = false;

    @Test
    void testHasExcelFormat() {
        ExcelProfileReader reader = new ExcelProfileReader();
        File fileToParse = new File(filePath);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MultipartFile multipartFile = new MockMultipartFile(
                "Network1_A_Bd.xlsx",
                fileToParse.getName(),
                FileUtils.CONTENT_TYPE.get("xlsx"),
                IOUtils.toByteArray(input)
            );
            boolean isExcelFormat = ExcelFileUtils.hasExcelFormat(multipartFile);
            assertThat(isExcelFormat).isEqualTo(Boolean.TRUE);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void testHasNoExcelFormat() {
        ExcelProfileReader reader = new ExcelProfileReader();
        File fileToParse = new File(filePathCsv);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MultipartFile multipartFile = new MockMultipartFile("test_15m.csv", fileToParse.getName(), null, IOUtils.toByteArray(input));
            boolean isExcelFormat = ExcelFileUtils.hasExcelFormat(multipartFile);
            assertThat(isExcelFormat).isEqualTo(Boolean.FALSE);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void testParseExcelLoadFile() {
        int expected = 200;
        int actual = -1;
        ExcelProfileReader reader = new ExcelProfileReader();
        try {
            Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelLoadProfileFile(filePath, headerEnabled);
            for (String key : mapSheet.keySet()) {
                actual = mapSheet.get(key).size();
            }

            assertEquals(expected, actual);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void testParseExcelGenFile() {
        String filePath = "src\\test\\resources\\excel_file\\Generation\\Network5_A_Bd_GEN.xlsx";
        int expected = 8;
        int actual = -1;
        ExcelProfileReader reader = new ExcelProfileReader();
        try {
            Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelGenProfileFile(filePath, headerEnabled);
            for (String key : mapSheet.keySet()) {
                actual = mapSheet.get(key).size();
            }

            assertEquals(expected, actual);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void testParseExcelFlexibilityFile() {
        String filePath = "src\\test\\resources\\excel_file\\Flexibility\\Network5_Su_Bd - Flex.xlsx";
        int expectedDecrease = 42;
        int expectedIncrease = 42;
        int actualDecreaseSheet = -1;
        int actualIncreaseSheet = -1;
        ExcelProfileReader reader = new ExcelProfileReader();
        try {
            Map<String, List<LoadGeneratorPower>> mapSheet = reader.parseExcelFlexProfileFile(filePath, headerEnabled);
            for (String key : mapSheet.keySet()) {
                String sheetName = key;
                switch (sheetName) {
                    case "Flex_decrease":
                        actualDecreaseSheet = mapSheet.get(key).size();
                        break;
                    default:
                        actualIncreaseSheet = mapSheet.get(key).size();
                        break;
                }
            }
            assertEquals(expectedDecrease, actualDecreaseSheet);
            assertEquals(expectedIncrease, actualIncreaseSheet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }
}
