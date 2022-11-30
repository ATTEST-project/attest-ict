package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.model.LoadGeneratorPower;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import com.attest.ict.helper.excel.util.ExcelProfilesFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelProfileReader {

    public final Logger log = LoggerFactory.getLogger(ExcelProfileReader.class);

    public ExcelProfileReader() {}

    public Map<String, List<LoadGeneratorPower>> parseExcelLoadProfileFile(String relativePath, boolean headerEnabled)
        throws ExcelReaderFileException, FileNotFoundException {
        return parseLoadGeneratorPowerProfile(
            new FileInputStream(new File(relativePath)),
            headerEnabled,
            ExcelProfilesFormat.LOAD_SHEET_NUM
        );
    }

    public Map<String, List<LoadGeneratorPower>> parseExcelGenProfileFile(String relativePath, boolean headerEnabled)
        throws ExcelReaderFileException, FileNotFoundException {
        return parseLoadGeneratorPowerProfile(
            new FileInputStream(new File(relativePath)),
            headerEnabled,
            ExcelProfilesFormat.GEN_SHEET_NUM
        );
    }

    public Map<String, List<LoadGeneratorPower>> parseExcelFlexProfileFile(String relativePath, boolean headerEnabled)
        throws ExcelReaderFileException, FileNotFoundException {
        return parseLoadGeneratorPowerProfile(
            new FileInputStream(new File(relativePath)),
            headerEnabled,
            ExcelProfilesFormat.FLEXIBILITY_SHEET_NUM
        );
    }

    // Load
    public Map<String, List<LoadGeneratorPower>> parseExcelLoadProfileFile(MultipartFile file, boolean headerEnabled)
        throws ExcelReaderFileException {
        try {
            return parseLoadGeneratorPowerProfile(file.getInputStream(), headerEnabled, ExcelProfilesFormat.LOAD_SHEET_NUM);
        } catch (IOException ioex) {
            throw new ExcelReaderFileException(ioex.getMessage(), ioex.getCause());
        }
    }

    // Gen
    public Map<String, List<LoadGeneratorPower>> parseExcelGenProfileFile(MultipartFile file, boolean headerEnabled)
        throws ExcelReaderFileException {
        try {
            return parseLoadGeneratorPowerProfile(file.getInputStream(), headerEnabled, ExcelProfilesFormat.GEN_SHEET_NUM);
        } catch (IOException ioex) {
            throw new ExcelReaderFileException(ioex.getMessage(), ioex.getCause());
        }
    }

    // Flexibility
    public Map<String, List<LoadGeneratorPower>> parseExcelFlexProfileFile(MultipartFile file, boolean headerEnabled)
        throws ExcelReaderFileException {
        try {
            return parseLoadGeneratorPowerProfile(file.getInputStream(), headerEnabled, ExcelProfilesFormat.FLEXIBILITY_SHEET_NUM);
        } catch (IOException ioex) {
            throw new ExcelReaderFileException(ioex.getMessage(), ioex.getCause());
        }
    }

    public Map<String, List<LoadGeneratorPower>> parseLoadGeneratorPowerProfile(
        InputStream inputStream,
        boolean headerEnabled,
        Integer[] sheetsIndex
    ) throws ExcelReaderFileException {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            Map<String, List<LoadGeneratorPower>> demandMap = Collections.synchronizedMap(
                new LinkedHashMap<String, List<LoadGeneratorPower>>()
            );
            for (int sheetIndex : sheetsIndex) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = sheet.getSheetName();
                log.debug("Reading sheet {}: ", sheetName);
                Iterator<Row> rows = sheet.iterator();
                List<LoadGeneratorPower> demandProfileList = new ArrayList<LoadGeneratorPower>();
                while (rows.hasNext()) {
                    List<Double> values = new ArrayList<Double>();
                    Row currentRow = rows.next();
                    // log.debug("Reading rowNum: {} ", currentRow.getRowNum());
                    // skip header
                    if (headerEnabled && currentRow.getRowNum() == 0) {
                        // log.debug("Skip header");
                        continue;
                    }

                    if (ExcelFileUtils.isEmptyRow(currentRow)) {
                        // log.debug("Skip Empty Row");
                        continue;
                    }

                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    LoadGeneratorPower demandProfile = new LoadGeneratorPower();
                    int cellIdx = 0;
                    // read all cell in row
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        CellType cellType = currentCell.getCellType();

                        if (cellType.equals(CellType.BLANK) && cellIdx == 0) {
                            log.debug("Row {} busNum is empty, skip row ", currentRow.getRowNum(), cellIdx);
                            break;
                        }
                        switch (cellIdx) {
                            case 0:
                                try {
                                    double cellval = currentCell.getNumericCellValue();
                                    log.debug(" bus num:{} ", cellval);
                                    demandProfile.setBusNum(Double.valueOf(cellval).longValue());
                                    break;
                                } catch (NumberFormatException nfe) {
                                    throw new ExcelnvalidDataException(
                                        "fail to parse Excel File, check data at sheetName: " +
                                        sheetName +
                                        ", row: " +
                                        currentRow.getRowNum() +
                                        ", cell: " +
                                        cellIdx
                                    );
                                }
                            case 1:
                                String powerType = currentCell.getStringCellValue().trim();
                                log.debug(" PowerType:{}*** ", powerType);
                                if (!ExcelProfilesFormat.POWER_TYPE.contains(powerType)) {
                                    throw new ExcelnvalidDataException(
                                        "fail to parse Excel File, check data at at sheetName: " +
                                        sheetName +
                                        ", row: " +
                                        currentRow.getRowNum() +
                                        ", cell:" +
                                        cellIdx +
                                        " possible values are: " +
                                        ExcelProfilesFormat.POWER_TYPE
                                    );
                                }
                                demandProfile.setPowerType(currentCell.getStringCellValue());
                                break;
                            default:
                                try {
                                    values.add(currentCell.getNumericCellValue());
                                    break;
                                } catch (NumberFormatException nfe) {
                                    throw new ExcelnvalidDataException(
                                        "fail to parse Excel File, check data at sheetName: " +
                                        sheetName +
                                        ", row: " +
                                        currentRow.getRowNum() +
                                        " cell:" +
                                        cellIdx
                                    );
                                }
                        }
                        cellIdx++;
                    }
                    demandProfile.setValues(values);
                    demandProfileList.add(demandProfile);
                    demandMap.put(sheetName, demandProfileList);
                }
                workbook.close();
            }

            return demandMap;
        } catch (IOException e) {
            throw new ExcelReaderFileException("fail to parse Excel file: " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("Error closing workbook: " + e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error closing fileInputStream " + e.getMessage());
                }
            }
        }
    }

    public static void main(String args[]) {
        ExcelProfileReader reader = new ExcelProfileReader();
        // String relativePath = "C:\\DOCUMENTI\\SoftLab\\ATTEST\\WP2\\delivered
        // networks and data\\PT_Dx_05_2020\\Load\\Network5_A_Bd.xlsx";
        // String relativePath = "C:\\DOCUMENTI\\SoftLab\\ATTEST\\WP2\\delivered
        // networks and data\\PT_Dx_05_2020\\Flexibility\\Network5_Su_Bd - Flex.xlsx";
        // String relativePath =
        // "src\\test\\resources\\excel_file\\Flexibility\\Network5_Su_Bd - Flex.xlsx";
        /*String relativePath =
            "C:\\DOCUMENTI\\SoftLab\\ATTEST\\WP2\\t2.3\\deliverables\\delivered networks and data\\UK_Dx_01_2020\\UK_Urban_Network.xlsx";

        Map<String, List<LoadGeneratorPower>> mapSheet;
        try {
            mapSheet = reader.parseExcelLoadProfileFile(relativePath, false);
            mapSheet.entrySet().forEach(entry -> System.out.println("     " + entry));
        } catch (ExcelReaderFileException | FileNotFoundException e) {
            e.printStackTrace();
        }
        */

        List<String> toRemove = Arrays.asList("Number of Contingencies", "Number of Scenario");

        Map<String, String> mapTest = new HashMap<String, String>();

        Map<String, String> mapNew = new HashMap<String, String>();

        mapTest.put("Number of Contingencies", "Number of Scenario");
        mapTest.put("Number of Scenario", "pppp");
        Set<String> keys = mapTest.keySet();
        for (String key : keys) {
            String newKey = key.toLowerCase().replaceAll(" ", "_");
            System.out.println(newKey);
            mapNew.put(newKey, mapTest.get(key));
        }

        for (Map.Entry<String, String> entry : mapNew.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
