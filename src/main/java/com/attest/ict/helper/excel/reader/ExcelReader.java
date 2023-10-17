package com.attest.ict.helper.excel.reader;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelReader {

    public final Logger log = LoggerFactory.getLogger(ExcelReader.class);
    private Workbook workbook = null;

    private String errMsg = "An error occurred while reading the Excel file: ";

    public ExcelReader(String relativePath) {
        try {
            this.workbook = WorkbookFactory.create(new File(relativePath));
        } catch (IOException e) {
            throw new ExcelReaderFileException(errMsg + e.getMessage());
        }
    }

    public ExcelReader(File relativePath) {
        try {
            this.workbook = WorkbookFactory.create(relativePath);
        } catch (IOException e) {
            this.closeWorkBook();
            throw new ExcelReaderFileException(errMsg + e.getMessage());
        }
    }

    public ExcelReader(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            this.workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            this.closeWorkBook();
            throw new ExcelReaderFileException(errMsg + e.getMessage());
        }
    }

    public ExcelReader(byte[] data) {
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            this.workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            this.closeWorkBook();
            throw new RuntimeException(e);
        }
    }

    public void closeWorkBook() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error("Error closing workbook: " + e.getMessage());
            }
        }
    }

    public Sheet getSheetByName(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public Sheet getSheetByIndex(int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }

    public String[] parseHeaderBySheetIndex(int sheetIndex) {
        List<String> headers = new ArrayList<>();
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        Row headerRow = sheet.getRow(0);
        Iterator<Cell> header = headerRow.iterator();
        while (header.hasNext()) {
            Cell cell = header.next();
            headers.add(ExcelFileUtils.getCellData(cell));
        }

        String[] headersArray = new String[headers.size()];
        headers.toArray(headersArray);
        return headersArray;
    }

    public String[] parseHeaderBySheetName(String sheetName, int indexHeader) {
        List<String> headers = new ArrayList<>();
        Sheet sheet = workbook.getSheet(sheetName);

        Row headerRow = sheet.getRow(indexHeader);
        Iterator<Cell> header = headerRow.iterator();
        while (header.hasNext()) {
            Cell cell = header.next();
            headers.add(ExcelFileUtils.getCellData(cell));
        }

        String[] headersArray = new String[headers.size()];
        headers.toArray(headersArray);
        return headersArray;
    }

    public Map<Integer, List<String>> parseRowData(Sheet sheet, boolean isHeaderExists, int indexHeader) {
        Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
        int rowIndex = 0;
        for (Row row : sheet) {
            rowIndex = row.getRowNum();

            if (isHeaderExists && rowIndex <= indexHeader) {
                // log.debug("rowIndex: {}, skip header:", rowIndex);
                continue; // skips first row
            }
            if (ExcelFileUtils.isEmptyRow(row)) {
                // log.debug("rowIndex: {} , skip empty row:", rowIndex);
                continue; // skips empty row
            }

            data.put(rowIndex, new ArrayList<String>());
            for (Cell cell : row) {
                data.get(rowIndex).add(ExcelFileUtils.getCellData(cell));
            }
        }
        return data;
    }

    /**
     * @param sheet          sheet to read
     * @param columnIndex    column to read
     * @param isHeaderExists true if sheet contains column's header
     * @return List of the column's values
     */
    public List<String> parseColumn(Sheet sheet, int columnIndex, boolean isHeaderExists) {
        List<String> columnValues = new ArrayList<String>();
        try {
            int numRow = sheet.getLastRowNum();
            int numCols = sheet.getRow(0).getLastCellNum();
            int rowMax = isHeaderExists ? numRow : numRow + 1;
            int firstRowValues = isHeaderExists ? 1 : 0;
            for (int rowIndex = firstRowValues; rowIndex <= numRow; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (ExcelFileUtils.isEmptyRow(row)) {
                    continue; // skips empty row
                }
                int colIndex = 0;
                for (Cell cell : row) {
                    if (colIndex == columnIndex && cell != null) {
                        columnValues.add(ExcelFileUtils.getCellData(cell));
                    }
                    colIndex++;
                }
            }
        } catch (Exception e) {
            throw new ExcelReaderFileException(errMsg + e.getMessage());
        }
        return columnValues;
    }

    /**
     * @return file's sheetName list
     */
    public List<String> getSheetsName() {
        List<String> sheetsName = new ArrayList<String>();
        Iterator<Sheet> sheets = this.workbook.sheetIterator();
        while (sheets.hasNext()) {
            Sheet sheet = sheets.next();
            sheetsName.add(sheet.getSheetName());
        }
        return sheetsName;
    }

    public static void main(String args[]) {
        //String relativePath = "C:\\ATTEST\\tools\\WP3\\T33\\data\\HR1\\Results\\HR1_planning_results.xls";
        //String relativePath =   "C:\\ATSIM\\WP3\\T33\\005dae36-2948-4c3e-8eb3-33e8978db21e\\HR1_PLOTS_TRUE\\Results\\HR1_planning_results.xls";
        String relativePath =
            "C:\\ATSIM\\WP3\\T33\\005dae36-2948-4c3e-8eb3-33e8978db21e\\HR1_PLOTS_TRUE\\Results\\CS1_planning_results.xlsx";
        //String relativePath =  "C:\\ATSIM\\WP3\\T33\\005dae36-2948-4c3e-8eb3-33e8978db21e\\HR1_PLOTS_TRUE\\Results\\hr1_planning_results_short.xls";
        String[] headers;
        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;

        boolean test5 = false;
        boolean test6 = true;
        if (test1) try {
            ExcelReader reader = new ExcelReader(relativePath);
            headers = reader.parseHeaderBySheetIndex(1);
            System.out.println("=========================== ");
            System.out.println(" Header: ");
            System.out.println(Arrays.toString(headers));
            System.out.println("=========================== ");
        } catch (ExcelReaderFileException e) {
            System.out.println("=======test 1 error ==== ");
            e.printStackTrace();
        }
        //--- test 2
        if (test2) try {
            ExcelReader reader = new ExcelReader(relativePath);
            int sheetIndex = 0;
            FileInputStream inputStream = new FileInputStream(new File(relativePath));
            Sheet sheet = reader.getSheetByIndex(sheetIndex);
            Map<Integer, List<String>> rowsData = reader.parseRowData(sheet, true, 0);
            System.out.println("=========================== ");
            System.out.println(" Values: ");
            System.out.println("  " + Arrays.deepToString(rowsData.entrySet().stream().toArray()));
            System.out.println("=========================== ");
        } catch (ExcelReaderFileException | FileNotFoundException e) {
            System.out.println("=======test 2 error ==== ");
            e.printStackTrace();
        }

        //--- test 3
        if (test3) try {
            int sheetIndex = 9;
            int columnIndex = 1;
            ExcelReader reader = new ExcelReader(relativePath);
            Sheet sheet = reader.getSheetByIndex(sheetIndex);
            List<String> values = reader.parseColumn(sheet, columnIndex, true);
            reader.closeWorkBook();

            List connectionNode = values
                .stream()
                .filter(s -> !(s).contains("-"))
                .map(s -> Double.valueOf(s).intValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            System.out.println("=========================== ");
            System.out.println(" Values: ");
            System.out.println("  " + connectionNode.toString());
            System.out.println("=========================== ");
        } catch (ExcelReaderFileException e) {
            System.out.println("=======test 4 error ==== ");
            e.printStackTrace();
        }
        if (test5) try {
            String sheetName = "Interface PF";
            ExcelReader reader = new ExcelReader(relativePath);
            Sheet sheet = reader.getSheetByName(sheetName);
            Map<Integer, List<String>> mapData = reader.parseRowData(sheet, true, 0);

            System.out.println("==========Read sheet by name =============== ");
            System.out.println(" Values: ");
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                System.out.println("  " + data.toString());
            }

            System.out.println("=========================== ");
        } catch (ExcelReaderFileException e) {
            System.out.println("=======test 5 error ==== ");
            e.printStackTrace();
        }

        if (test6) try {
            String sheetName = "Power Flows";
            ExcelReader reader = new ExcelReader(relativePath);
            Sheet sheet = reader.getSheetByName(sheetName);
            Map<Integer, List<String>> data = reader.parseRowData(sheet, true, 0);

            System.out.println("==========Read sheet by name =============== ");
            System.out.println(" Values: ");
            System.out.println("  " + Arrays.toString(data.values().toArray()));
            System.out.println("=========================== ");
        } catch (ExcelReaderFileException e) {
            System.out.println("=======test 5 error ==== ");
            e.printStackTrace();
        }
    }
}
