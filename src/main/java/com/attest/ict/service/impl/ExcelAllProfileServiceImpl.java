package com.attest.ict.service.impl;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchElVal;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.TransfElVal;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.exception.ExcelnvalidDataException;
import com.attest.ict.helper.excel.util.ExcelFileUtils;
import com.attest.ict.helper.excel.util.ExcelProfilesFormat;
import com.attest.ict.repository.BranchElValRepository;
import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.repository.TransfElValRepository;
import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.ExcelAllProfileService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.service.mapper.NetworkMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ExcelAllProfileServiceImpl implements ExcelAllProfileService {

    public final Logger LOGGER = LoggerFactory.getLogger(ExcelAllProfileServiceImpl.class);

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    InputFileMapper inputFileMapper;

    @Autowired
    LoadElValRepository loadElValRepository;

    @Autowired
    LoadProfileRepository loadProfileRepository;

    @Autowired
    InputFileService inputFileServiceImpl;

    @Autowired
    InputFileRepository inputFileRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    GenProfileRepository genProfileRepository;

    @Autowired
    GenElValRepository genElValRepository;

    @Autowired
    TransfProfileRepository transfProfileRepository;

    @Autowired
    TransfElValRepository transfElValRepository;

    @Autowired
    BranchProfileRepository branchProfileRepository;

    @Autowired
    BranchElValRepository branchElValRepository;

    @Override
    public void allProfile(MultipartFile file, Network network, Integer mode, String season, String typicalDay) {
        // header is present in file
        boolean isHeaderEnabled = Boolean.TRUE.booleanValue();
        Long networkId = network.getId();
        LOGGER.info(
            "allProfile() - parse the auxiliary file: {} for networkId: {}, mode: {}, season: {}, typicalDay: {} ",
            file.getOriginalFilename(),
            networkId,
            mode,
            season,
            typicalDay
        );
        Workbook workbook = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            workbook = new XSSFWorkbook(inputStream);

            Map<String, LoadElVal> loadElMap = Collections.synchronizedMap(new LinkedHashMap<String, LoadElVal>());

            Map<String, GenElVal> genElMap = Collections.synchronizedMap(new LinkedHashMap<String, GenElVal>());

            Map<String, TransfElVal> transfElMap = Collections.synchronizedMap(new LinkedHashMap<String, TransfElVal>());

            Map<String, BranchElVal> branchElMap = Collections.synchronizedMap(new LinkedHashMap<String, BranchElVal>());

            // Sheets' Header are different between PT test cases and HR below all
            // combination:
            // 0: "Load P (MW)",
            // 1: "Load Q (Mvar)",
            // 2: "Gen Status",
            // 3: "Gen P (MW)",
            // 4: "Gen Q (Mvar)",
            // 5: "Gen Vg (pu)",
            // 6: "Gen Vg (p.u.)",
            // 7: "Transf Tap Ratio",
            // 8: "Transformer Tap Ratio",
            // 9: "Transformer Status",
            // 1:0 "Branch Status",
            // 11: "Transf Data",
            // 12: "Lines_Cables Data",
            // 13: "Auxiliary Data",
            // 14: "Downward flexibility",
            // 15: "Upward flexibility",

            for (int i = 0; i < ExcelProfilesFormat.TX_ALL_SHEETS_NAME.length; i++) {
                String sheetName = ExcelProfilesFormat.TX_ALL_SHEETS_NAME[i];
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    LOGGER.debug(" Sheet: {} doesn't exists ", sheetName);
                } else {
                    switch (i) {
                        case 0:
                            parseLoad(networkId, sheet, loadElMap, ExcelProfilesFormat.ACTIVE_POWER);
                            break;
                        case 1:
                            parseLoad(networkId, sheet, loadElMap, ExcelProfilesFormat.REACTIVE_POWER);
                            break;
                        case 2:
                            // Status
                            parseGen(networkId, sheet, genElMap, ExcelProfilesFormat.GEN_STATUS);
                            break;
                        case 3:
                            // Active power
                            parseGen(networkId, sheet, genElMap, ExcelProfilesFormat.ACTIVE_POWER);
                            break;
                        case 4:
                            // ReActive power
                            parseGen(networkId, sheet, genElMap, ExcelProfilesFormat.REACTIVE_POWER);
                            break;
                        case 5:
                        case 6:
                            // Voltage Magnitude
                            parseGen(networkId, sheet, genElMap, ExcelProfilesFormat.VOLTAGE_MAGNITUDE);
                            break;
                        case 7:
                        case 8:
                            // "Transf Tap Ratio", "Transformer Tap Ratio",
                            parseTransf(networkId, sheet, transfElMap, ExcelProfilesFormat.TRANSF_TAP_RATIO);
                            break;
                        case 9:
                            // Transformer Status
                            parseTransf(networkId, sheet, transfElMap, ExcelProfilesFormat.TRANSF_STATUS);
                            break;
                        case 10:
                            // Branch Status
                            parseBranch(networkId, sheet, branchElMap, ExcelProfilesFormat.BRANCH_STATUS);
                            break;
                        // stand by, attenting to understand if this data should be used by someone
                        case 11:
                            // Transf Data
                            break;
                        case 12:
                            // Lines_Cables Data",
                            break;
                        case 13:
                            // "Auxiliary Data",
                            break;
                        case 14:
                        case 15:
                        // "Downward flexibility", "Upward flexibility"
                        default:
                            break;
                    }
                }
            } // end for all sheets

            // SavefIle in table: inputFile
            InputFileDTO inputFileDto = inputFileServiceImpl.saveFileForNetworkWithDescr(
                file,
                networkMapper.toDto(network),
                AttestConstants.INPUT_FILE_ALL_PROFILE_DESCR
            );
            LOGGER.info("allProfile() - New File: {}, saved successfully in InputFile ", inputFileDto.getFileName());

            // Save Data on DB
            if (!genElMap.isEmpty()) {
                saveGenProfiles(inputFileDto, network, mode, season, typicalDay, ExcelProfilesFormat.TX_TIME_INTERVAL, genElMap);
            }

            if (!loadElMap.isEmpty()) {
                saveLoadProfiles(inputFileDto, network, mode, season, typicalDay, ExcelProfilesFormat.TX_TIME_INTERVAL, loadElMap);
            }

            if (!transfElMap.isEmpty()) {
                saveTransfProfile(inputFileDto, network, mode, season, typicalDay, ExcelProfilesFormat.TX_TIME_INTERVAL, transfElMap);
            }

            if (!branchElMap.isEmpty()) {
                saveBranchProfile(inputFileDto, network, mode, season, typicalDay, ExcelProfilesFormat.TX_TIME_INTERVAL, branchElMap);
            }
        } catch (ExcelReaderFileException ex) {
            LOGGER.error("allProfile() - Exception reading file: {}", ex.getMessage());
        } catch (IOException e) {
            throw new ExcelReaderFileException("fail to parse Excel File:  " + file.getOriginalFilename());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOGGER.error("allProfile() - Error closing workbook: " + e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("allProfile() - Error closing inputStream " + e.getMessage());
                }
            }
        }
    }

    private void parseTransf(Long networkId, Sheet sheet, Map<String, TransfElVal> elValMap, String valueType) {
        String sheetName = sheet.getSheetName();
        LOGGER.info("parseTransf() - Reading sheet {}: ", sheetName);

        Iterator<Row> rows = sheet.iterator();
        Long fromBusNum = null;
        Long toBusNum = null;
        Double value = null;
        String voltage = null; // present only for HR TX profile .xlsx
        Long transfIdOnSubstation = null; // present only for HR TX profile .xlsx
        int hour = 0;

        // Columns' Title
        List<String> headers = new ArrayList<String>();
        String columnTitle = "";

        String fbusToBusPrec = null;

        // Transformer are save on branch's table
        List<Branch> branchList = new ArrayList<Branch>();
        int branchOrder = 0;
        String fbusToBus = "";

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // Skip empty row
            if (ExcelFileUtils.isEmptyRow(currentRow)) {
                LOGGER.debug("RowNum: {}  is empty.Skip ", currentRow.getRowNum());
                continue; // skips empty row
            }

            // Reading Header
            if (currentRow.getRowNum() == 0) {
                int cellIdx = 0;

                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    headers.add(currentCell.getStringCellValue());
                }
            }
            // Reading other Rows
            else {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int min = 0; // sheet contains time series every hour of a day at 00
                int columnsToSkip = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    // LOGGER.debug(" Row: {}, Col: {}, fbusToBusPrec: {}, branchOrder:{} ", currentRow.getRowNum(),headers.get(cellIdx),  fbusToBusPrec, branchOrder, columnsToSkip);
                    hour = cellIdx;
                    if (cellIdx <= 3) {
                        columnTitle = ExcelProfilesFormat.TX_BRANCH_TRANSF_COMMON_HEADER[cellIdx];
                    }

                    switch (cellIdx) {
                        // -- col 0 = FBus
                        case 0:
                            try {
                                double cellval = currentCell.getNumericCellValue();
                                fromBusNum = Double.valueOf(cellval).longValue();
                                columnsToSkip++;
                                LOGGER.debug("Case 0 - Read FBus: {}", fromBusNum);
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                            break;
                        // -- col 1 = TBus
                        case 1:
                            try {
                                double cellval = currentCell.getNumericCellValue();
                                toBusNum = Double.valueOf(cellval).longValue();
                                fbusToBus = fromBusNum + "_" + toBusNum;
                                columnsToSkip++;
                                LOGGER.debug("Case 1 - Read TBus: {}", toBusNum);
                                if (fbusToBusPrec != null && fbusToBusPrec.equals(fbusToBus)) {
                                    branchOrder++; // more transformer on the same branch
                                } else {
                                    // branch change restart branchOrder
                                    branchOrder = 0;
                                    branchList =
                                        branchRepository.findByFbusAndTbusAndNetworkIdOrderByIdAsc(fromBusNum, toBusNum, networkId);
                                    if (branchList == null) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse Excel File, check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ",Branch Fbus: " +
                                            fromBusNum +
                                            ",  toBus: " +
                                            toBusNum +
                                            " not found for network id: " +
                                            networkId
                                        );
                                    }
                                }
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                        // -- transformer value (tap ratio or status ) at 00:00 or nominal Voltage in
                        // case of HR test cases file
                        case 2:
                            // read Nominal Voltage
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                    int cellVal = (int) currentCell.getNumericCellValue();
                                    voltage = "" + cellVal;
                                } else {
                                    voltage = currentCell.getStringCellValue();
                                }
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 2 - Reading column[{}], columnTitle: {} Reading Nominal Voltage {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    voltage
                                );
                            } else {
                                //value = currentCell.getNumericCellValue();
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case 2 - Reading column[{}]  Transf hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                setTransfElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        // -- transformer value (tap ratio or status ) at 01:00 or TransformeId On
                        // Substation in case of HR test cases file
                        case 3:
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                double cellval = currentCell.getNumericCellValue();
                                transfIdOnSubstation = Double.valueOf(cellval).longValue();
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 3 - Reading column [{}], columnTitle: {} Reading ID {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    transfIdOnSubstation
                                );
                            } else {
                                //value = currentCell.getNumericCellValue();
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case 3 - Reading column[{}]  Transf hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                setTransfElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        default: // -- transformer value (tap ratio or status ) for hour >= '02:00'
                            try {
                                // value = currentCell.getNumericCellValue();
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case Default - Reading column[{}]  Transf hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                if (voltage != null && transfIdOnSubstation != null) {
                                    setTransfElVal(
                                        branch,
                                        voltage,
                                        transfIdOnSubstation,
                                        hour - columnsToSkip,
                                        min,
                                        value,
                                        elValMap,
                                        valueType
                                    );
                                } else {
                                    setTransfElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                                }

                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    " cell:" +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                    }

                    cellIdx++;
                    fbusToBusPrec = fbusToBus;
                }
            }
        }
        LOGGER.debug("parseTransf() - Exit");
    }

    private void setTransfElVal(Branch branch, int hour, int min, double value, Map<String, TransfElVal> elValMap, String valueType) {
        String key = branch.getId() + "_" + hour + "_" + min;

        TransfElVal elVal = elValMap.get(key);
        if (elVal == null) {
            elVal = new TransfElVal();
            elVal.setBranch(branch);
            elVal.setHour(hour);
            elVal.setMin(min);
        }

        switch (valueType) {
            case ExcelProfilesFormat.TRANSF_STATUS:
                // Transformer Status spreadSheet
                int status = (int) value;
                elVal.setStatus(status);
                break;
            case ExcelProfilesFormat.TRANSF_TAP_RATIO:
                // Transformer Tap Ration
                elVal.setTapRatio(value);
                break;
        }

        elValMap.put(key, elVal);
    }

    private void setTransfElVal(
        Branch branch,
        String nominalVoltage,
        Long idOnSubstation,
        int hour,
        int min,
        Double value,
        Map<String, TransfElVal> elValMap,
        String valueType
    ) {
        String key = branch.getId() + "_" + hour + "_" + min;

        TransfElVal elVal = elValMap.get(key);
        if (elVal == null) {
            elVal = new TransfElVal();
            elVal.setBranch(branch);
            elVal.setHour(hour);
            elVal.setMin(min);
            elVal.setNominalVoltage(nominalVoltage);
            elVal.setTrasfIdOnSubst(idOnSubstation);
        }

        switch (valueType) {
            case ExcelProfilesFormat.TRANSF_STATUS:
                // Transformer Status spreadSheet
                Integer status = (value != null) ? value.intValue() : null;
                elVal.setStatus(status);
                break;
            case ExcelProfilesFormat.TRANSF_TAP_RATIO:
                // Transformer Tap Ratio
                elVal.setTapRatio(value);
                break;
        }

        elValMap.put(key, elVal);
    }

    private void parseBranch(Long networkId, Sheet sheet, Map<String, BranchElVal> elValMap, String valueType) {
        String sheetName = sheet.getSheetName();
        LOGGER.info("parseBranch() - Reading sheet {}: ", sheetName);

        Iterator<Row> rows = sheet.iterator();

        Long fromBusNum = null;
        Long toBusNum = null;

        Double value = null;
        String voltage = null; // present only for HR TX profile .xlsx
        Long idOnSubstation = null; // present only for HR TX profile .xlsx
        int hour = 0;

        // Columns' Title
        List<String> headers = new ArrayList<String>();

        String fbusToBusPrec = null;

        // Transformer are save on branch's table
        List<Branch> branchList = new ArrayList<Branch>();
        int branchOrder = 0;
        String fbusToBus = "";
        String columnTitle = "";

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // Skip empty row
            if (ExcelFileUtils.isEmptyRow(currentRow)) {
                LOGGER.debug("RowNum: {}  is empty.Skip ", currentRow.getRowNum());
                continue;
            }
            // Reading Header
            if (currentRow.getRowNum() == 0) {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    headers.add(currentCell.getStringCellValue());
                }
            }
            // Reading other Rows
            else {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int min = 0; // sheet contains time series every hour of a day at 00
                int columnsToSkip = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    hour = cellIdx;
                    if (cellIdx <= 3) {
                        columnTitle = ExcelProfilesFormat.TX_BRANCH_TRANSF_COMMON_HEADER[cellIdx];
                    }

                    switch (cellIdx) {
                        // -- col 0 = FBus
                        case 0:
                            try {
                                double cellval = currentCell.getNumericCellValue();
                                fromBusNum = Double.valueOf(cellval).longValue();
                                LOGGER.debug("Case 0 - Read FBus: {}", fromBusNum);
                                columnsToSkip++;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                            break;
                        // -- col 1 = TBus
                        case 1:
                            try {
                                double cellval = currentCell.getNumericCellValue();
                                toBusNum = Double.valueOf(cellval).longValue();
                                fbusToBus = fromBusNum + "_" + toBusNum;
                                columnsToSkip++;
                                LOGGER.debug("Case 1 - Read TBus: {}", toBusNum);
                                if (fbusToBusPrec != null && fbusToBusPrec.equals(fbusToBus)) {
                                    branchOrder++; // more transformer on the same branch
                                } else {
                                    // branch change, restart branchOrder
                                    branchOrder = 0;
                                    branchList =
                                        branchRepository.findByFbusAndTbusAndNetworkIdOrderByIdAsc(fromBusNum, toBusNum, networkId);
                                    if (branchList == null) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse Excel File, check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ",Branch Fbus: " +
                                            fromBusNum +
                                            ",  toBus: " +
                                            toBusNum +
                                            " not found for network id: " +
                                            networkId
                                        );
                                    }
                                }
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                        // -- status at 00:00 or Voltage in case of HR test cases
                        case 2:
                            // read Nominal Voltage
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                    int cellVal = (int) currentCell.getNumericCellValue();
                                    voltage = "" + cellVal;
                                } else {
                                    voltage = currentCell.getStringCellValue();
                                }
                                columnsToSkip++;
                            } else {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case 2 - Reading column[{}]  Branch hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                setBranchElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        // -- status at 01:00 or branchId On Substation in case of HR test cases
                        case 3:
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                double cellval = currentCell.getNumericCellValue();
                                idOnSubstation = Double.valueOf(cellval).longValue();
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 3 - Reading column [{}], columnTitle: {} Reading ID {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    idOnSubstation
                                );
                            } else {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case 3 - Reading column[{}]  Branch hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                setBranchElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        default: // timeSeries branch status for hour > 2
                            try {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case Default - Reading column[{}]  Branch hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Branch branch = branchList.get(branchOrder);
                                if (voltage != null && idOnSubstation != null) {
                                    setBranchElVal(branch, voltage, idOnSubstation, hour - columnsToSkip, min, value, elValMap, valueType);
                                } else {
                                    setBranchElVal(branch, hour - columnsToSkip, min, value, elValMap, valueType);
                                }

                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    " cell:" +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                    }

                    cellIdx++;
                    fbusToBusPrec = fbusToBus;
                }
            }
        }
        LOGGER.debug("parseBranch() - Exit");
    }

    private void setBranchElVal(Branch branch, int hour, int min, double value, Map<String, BranchElVal> elValMap, String valueType) {
        String key = branch.getId() + "_" + hour + "_" + min;
        BranchElVal elVal = elValMap.get(key);

        if (elVal == null) {
            elVal = new BranchElVal();
            elVal.setBranch(branch);
            elVal.setHour(hour);
            elVal.setMin(min);
        }

        // at the moment only one spreadSheet: 'Branch status" is provided for TX test
        // cases HR (Location1.xlsx, Location2.xlsx, Location3.xlsx)
        if (valueType.equals(ExcelProfilesFormat.BRANCH_STATUS)) {
            int status = (int) value;
            elVal.setStatus(status);
        }

        elValMap.put(key, elVal);
    }

    private void setBranchElVal(
        Branch branch,
        String nominalVoltage,
        Long idOnSubstation,
        int hour,
        int min,
        double value,
        Map<String, BranchElVal> elValMap,
        String valueType
    ) {
        String key = branch.getId() + "_" + hour + "_" + min;

        BranchElVal elVal = elValMap.get(key);
        if (elVal == null) {
            elVal = new BranchElVal();
            elVal.setBranch(branch);
            elVal.setHour(hour);
            elVal.setMin(min);
            elVal.setNominalVoltage(nominalVoltage);
            elVal.setBranchIdOnSubst(idOnSubstation);
        }

        // at the moment only one spreadSheet: 'Branch status" is provided for TX test
        // cases HR (Location1.xlsx, Location2.xlsx, Location3.xlsx)
        if (valueType.equals(ExcelProfilesFormat.BRANCH_STATUS)) {
            // Branch Status spreadSheet
            int status = (int) value;
            elVal.setStatus(status);
        }

        elValMap.put(key, elVal);
    }

    private void parseLoad(Long networkId, Sheet sheet, Map<String, LoadElVal> elValMap, String powerType) {
        String sheetName = sheet.getSheetName();
        LOGGER.info("parseLoad() - Reading sheet {}: ", sheetName);
        Iterator<Row> rows = sheet.iterator();
        Long busNum = null;
        Double value = null;
        String voltage = null; // present only for HR TX profile .xlsx
        Long loadIdOnSubstation = null; // present only for HR TX profile .xlsx
        int hour = 0;

        // Columns' Title
        List<String> headers = new ArrayList<String>();
        Long precBusNum = Long.valueOf(-1);
        Bus bus = null;
        String columnTitle = "";

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // Skip empty row
            if (ExcelFileUtils.isEmptyRow(currentRow)) {
                LOGGER.debug("RowNum: {}  is empty.Skip ", currentRow.getRowNum());
                continue; // skips empty row
            }
            // Reading Header
            if (currentRow.getRowNum() == 0) {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    headers.add(currentCell.getStringCellValue());
                }
            }
            // Reading other Rows
            else {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int min = 0; // sheet contains time series every hour of a day at 00
                int columnsToSkip = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    // LOGGER.debug(" Row: {} Col: {} ", currentRow.getRowNum(), cellIdx );
                    hour = cellIdx;
                    if (cellIdx <= 2) {
                        columnTitle = ExcelProfilesFormat.TX_BUS_GEN_COMMON_HEADER[cellIdx];
                    }

                    switch (cellIdx) {
                        // BUS NUM
                        case 0:
                            try {
                                double cellval = currentCell.getNumericCellValue();
                                busNum = Double.valueOf(cellval).longValue();
                                LOGGER.debug("Case 0 - Read Bus Num: {}", busNum);
                                columnsToSkip++;
                                if (!precBusNum.equals(busNum)) {
                                    bus = busRepository.findByBusNumAndNetworkId(busNum, networkId);
                                    if (bus == null) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse Excel File, check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ", busNum: " +
                                            busNum +
                                            " not found for network id: " +
                                            networkId
                                        );
                                    }
                                    precBusNum = busNum;
                                }
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                        // time series 00:00 or Voltage in case of HR test cases
                        case 1:
                            // read nominal Voltage
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                    int cellVal = (int) currentCell.getNumericCellValue();
                                    voltage = "" + cellVal;
                                } else {
                                    voltage = currentCell.getStringCellValue();
                                }
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 1 - Reading column [{}], columnTitle:{} Reading Nominal Voltage {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    voltage
                                );
                            } else {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case 1 - Reading column [{}] Load for  hour: {}, columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                setLoadElVal(bus, hour - columnsToSkip, min, value, elValMap, powerType);
                            }
                            break;
                        // time series 01:00 or ID in case of HR test cases
                        case 2:
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                double cellval = currentCell.getNumericCellValue();
                                loadIdOnSubstation = Double.valueOf(cellval).longValue();
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 2 - Reading column[{}]  ColumnTitle:{},  CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    cellval
                                );
                            } else {
                                value = currentCell.getNumericCellValue();
                                setLoadElVal(bus, hour - columnsToSkip, min, value, elValMap, powerType);
                            }
                            break;
                        default: // timeSeries case >=3
                            try {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case Default - Reading column[{}]  Load for  hour: {}  columnsToSkip: {} CellVal: {} Nominal Voltage: {} loadIdOnSubstation: {}",
                                    headers.get(cellIdx),
                                    hour,
                                    columnsToSkip,
                                    value,
                                    voltage,
                                    loadIdOnSubstation
                                );

                                if (voltage != null && loadIdOnSubstation != null) {
                                    setLoadElVal(bus, voltage, loadIdOnSubstation, hour - columnsToSkip, min, value, elValMap, powerType);
                                } else {
                                    setLoadElVal(bus, hour - columnsToSkip, min, value, elValMap, powerType);
                                }
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    " cell:" +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                    }

                    cellIdx++;
                }
            }
        }
        LOGGER.debug("parseLoad() -exit");
    }

    private void setLoadElVal(
        Bus bus,
        String nominalVoltage,
        Long loadIdOnSubstation,
        int hour,
        int min,
        Double value,
        Map<String, LoadElVal> readMap,
        String powerType
    ) {
        Long busId = bus.getId();
        // sambe bus more attached load identify by loadIdOnSubstation
        String key = busId + "_" + loadIdOnSubstation + "_" + hour + "_" + min;

        LoadElVal loadElVal = readMap.get(key);
        if (loadElVal == null) {
            loadElVal = new LoadElVal();
            loadElVal.setBus(bus);
            loadElVal.setHour(hour);
            loadElVal.setMin(min);
            loadElVal.setLoadIdOnSubst(loadIdOnSubstation);
            loadElVal.setNominalVoltage(nominalVoltage);
        }
        // Load P spreadSheet
        if (powerType.equals(ExcelProfilesFormat.POWER_TYPE.get(0))) {
            loadElVal.setP(value);
        } else {
            // Load Q spreadSheet
            loadElVal.setQ(value);
        }
        readMap.put(key, loadElVal);
    }

    private void setLoadElVal(Bus bus, int hour, int min, Double value, Map<String, LoadElVal> readMap, String powerType) {
        Long busId = bus.getId();

        String key = busId + "_" + hour + "_" + min;

        LoadElVal loadElVal = readMap.get(key);
        if (loadElVal == null) {
            loadElVal = new LoadElVal();
            loadElVal.setBus(bus);
            loadElVal.setHour(hour);
            loadElVal.setMin(min);
        }
        // Load P spreadSheet
        if (powerType.equals(ExcelProfilesFormat.POWER_TYPE.get(0))) {
            loadElVal.setP(value);
        } else {
            // Load Q spreadSheet
            loadElVal.setQ(value);
        }
        readMap.put(key, loadElVal);
    }

    /**
     * @param networkId
     * @param sheet
     * @param elValMap
     * @param valueType possible values are: P, Q, Status, Vg
     */
    private void parseGen(Long networkId, Sheet sheet, Map<String, GenElVal> elValMap, String valueType) {
        String sheetName = sheet.getSheetName();
        LOGGER.info("parseGen() - Reading sheet {}: ", sheetName);

        String columnTitle = "";
        List<String> headers = new ArrayList<String>();
        Long busNum = null;
        Double value = null;
        String voltage = null; // present only for HR TX profile .xlsx
        String type = null; // present only for HR TX Su_Bd profile .xlsx
        Long genIdOnSubstation = null; // present only for HR TX profile .xlsx
        int hour = 0;
        Long busNumPrec = null;
        List<Generator> generators = new ArrayList<Generator>();
        int busOrder = 0;

        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // Skip empty row
            if (ExcelFileUtils.isEmptyRow(currentRow)) {
                LOGGER.debug("RowNum: {}  is empty.Skip ", currentRow.getRowNum());
                continue; // skips empty row
            }

            // Reading Header
            if (currentRow.getRowNum() == 0) {
                int cellIdx = 0;

                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    String currentTitle = currentCell.getStringCellValue();
                    headers.add(currentCell.getStringCellValue());
                }
            }
            // Reading other Rows
            else {
                int cellIdx = 0;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int min = 0; // sheet contains time series every hour of a day at 00
                int columnsToSkip = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    hour = cellIdx;

                    if (cellIdx <= 3) {
                        // { "Bus Number", "Un (kV)", "ID", "Type" };
                        columnTitle = ExcelProfilesFormat.TX_BUS_GEN_COMMON_HEADER[cellIdx];
                    }

                    switch (cellIdx) {
                        // BUS NUM
                        case 0:
                            try {
                                //Double cellval = ExcelFileUtils.getDoubleCellValue(currentCell);
                                Double cellval = currentCell.getNumericCellValue();
                                busNum = cellval.longValue();
                                LOGGER.debug("Case 0 - Read Bus Num: {}", busNum);
                                columnsToSkip++;
                                if (busNumPrec != null && busNumPrec.compareTo(busNum) == 0) {
                                    busOrder++; // more generator on the same bus
                                } else {
                                    // bus change restart busOrder
                                    busOrder = 0;
                                    generators = generatorRepository.findByBusNumAndNetworkIdOrderByIdAsc(busNum, networkId);
                                    if (generators == null) {
                                        throw new ExcelnvalidDataException(
                                            "fail to parse Excel File, check data at sheetName: " +
                                            sheetName +
                                            ", row: " +
                                            currentRow.getRowNum() +
                                            ", generator on busNum: " +
                                            busNum +
                                            "  not found for network id: " +
                                            networkId
                                        );
                                    }
                                }
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    ", cell: " +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                        // time series 00:00 or Voltage in case of HR test cases
                        case 1:
                            // read nominal Voltage
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                    int cellVal = (int) currentCell.getNumericCellValue();
                                    voltage = "" + cellVal;
                                } else {
                                    voltage = currentCell.getStringCellValue();
                                }
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 1 - Reading column [{}], columnTitle:{} Reading Nominal Voltage {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    voltage
                                );
                            } else {
                                value = currentCell.getNumericCellValue();
                                LOGGER.debug(
                                    "Case 1 - Reading column [{}] GenVal: hour: {},  columnsToSkip: {},  CellVal: {}",
                                    headers.get(cellIdx),
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Generator gen = generators.get(busOrder);
                                setGenElVal(gen, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        // time series value at 01:00 or ID in case of HR test cases
                        case 2:
                            if (headers.get(cellIdx).equalsIgnoreCase(columnTitle)) {
                                Double cellval = currentCell.getNumericCellValue();
                                genIdOnSubstation = cellval.longValue();
                                columnsToSkip++;
                                LOGGER.debug(
                                    "Case 2 - Reading column[{}]  ColumnTitle:{},  CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    cellval
                                );
                            } else {
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case 2 - Reading column[: {}] ColumnTitle:{}, Gen   hour: {},  columnsToSkip: {}, CellVal: {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Generator gen = generators.get(busOrder);
                                setGenElVal(gen, hour - columnsToSkip, min, value, elValMap, valueType);
                            }
                            break;
                        // time series value at 02:00 or "" in case of HR test cases HOPS data 15_07_2020_Location1_Su_Bd.xlsx
                        case 3:
                            if (headers.get(cellIdx).trim().equalsIgnoreCase(columnTitle)) {
                                if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                    int cellVal = (int) currentCell.getNumericCellValue();
                                    type = "" + cellVal;
                                } else {
                                    type = currentCell.getStringCellValue();
                                }
                                LOGGER.debug(
                                    "Case 3 - Reading column:[{}]  columnTitle:[{}]  type: {} ",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    type
                                );
                                columnsToSkip++;
                            } else {
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case 3 - Reading column[{}]  Gen  hour: {}  columnsToSkip: {} CellVal : {}",
                                    headers.get(cellIdx),
                                    columnTitle,
                                    hour,
                                    columnsToSkip,
                                    value
                                );
                                Generator gen = generators.get(busOrder);
                                setGenValues(columnsToSkip, gen, voltage, genIdOnSubstation, hour, min, value, elValMap, valueType);
                            }
                            break;
                        default: // timeSeries
                            try {
                                value = this.getNumericCellValue(currentCell);
                                LOGGER.debug(
                                    "Case Default - Reading column[ {}]  Gen  hour: {}  columnsToSkip: {} CellVal : {} voltage: {} genIdOnSubstation {}",
                                    headers.get(cellIdx),
                                    hour,
                                    columnsToSkip,
                                    value,
                                    voltage,
                                    genIdOnSubstation
                                );
                                Generator gen = generators.get(busOrder);
                                setGenValues(columnsToSkip, gen, voltage, genIdOnSubstation, hour, min, value, elValMap, valueType);
                                break;
                            } catch (NumberFormatException nfe) {
                                String msg =
                                    "fail to parse Excel File, check data at sheetName: " +
                                    sheetName +
                                    ", row: " +
                                    currentRow.getRowNum() +
                                    " cell:" +
                                    cellIdx;
                                throw new ExcelnvalidDataException(msg, nfe);
                            }
                    }
                    cellIdx++;
                    busNumPrec = busNum;
                }
            }
        }
        LOGGER.debug("ParseGen() - exit ");
    }

    private boolean isNumeric(String valStr) {
        try {
            Double doubleVal = Double.parseDouble(valStr);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private Double getNumericCellValue(Cell cell) {
        Double val = null;
        String msg = " CellValue {} is not a number. Return null!";
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    // LOGGER.debug(" CellCell type NUMERIC, Value: {} ", cell.getNumericCellValue() );
                    val = Double.valueOf(cell.getNumericCellValue());
                    // LOGGER.debug(" CellCell type NUMERIC, Value: {} ", val );
                    break;
                case STRING:
                    // LOGGER.debug(" CellCell type STRING, Value: {} ", cell.getStringCellValue() );
                    if (isNumeric(cell.getStringCellValue())) {
                        val = Double.valueOf(cell.getStringCellValue());
                    } else {
                        String valStr = cell.getStringCellValue();
                        // HOPS data 15_07_2020_Location1_Su_Bd.xlsx GEN Q sheet, contains some cells with string value instead of number
                        if (valStr.contains(",")) {
                            String newVal = valStr.replace(",", ".");
                            if (isNumeric(newVal)) {
                                val = Double.valueOf(newVal);
                            }
                        }
                    }
                    //LOGGER.debug(" CellCell type STRING, Converted Value: {} ", val );
                    break;
                case BLANK:
                //  LOGGER.debug(" CellCell type BLANK,  Value: {}; -  return null", cell.getStringCellValue() );
            }
        } catch (Exception e) {
            LOGGER.error(
                "getNumericCellValue() - Error reading excel file. CellValue {} is not a number. Return null. Error: {}",
                cell,
                e.getMessage()
            );
        }
        // LOGGER.debug(" Cell Type: {} "+ cell.getCellType()  + " return  " +val);
        return val;
    }

    private void setGenValues(
        int columnsToSkip,
        Generator gen,
        String voltage,
        Long genIdOnSubstation,
        int hour,
        int min,
        Double value,
        Map<String, GenElVal> elValMap,
        String valueType
    ) {
        if (voltage != null && genIdOnSubstation != null) {
            LOGGER.debug(
                "----- Save A Gen for  hour: {}  CellVal : {} voltage: {} genIdOnSubstation {}",
                hour - columnsToSkip,
                value,
                voltage,
                genIdOnSubstation
            );
            setGenElVal(gen, voltage, genIdOnSubstation, hour - columnsToSkip, min, value, elValMap, valueType);
        } else {
            LOGGER.debug("----- Save B Gen  for hour: {}  CellVal : {} ", hour - columnsToSkip, value);
            setGenElVal(gen, hour - columnsToSkip, min, value, elValMap, valueType);
        }
    }

    private void setGenElVal(Generator gen, int hour, int min, Double value, Map<String, GenElVal> elValMap, String valueType) {
        String key = gen.getId() + "_" + hour + "_" + min;

        GenElVal genElVal = elValMap.get(key);
        if (genElVal == null) {
            genElVal = new GenElVal();
            genElVal.generator(gen);
            genElVal.setHour(hour);
            genElVal.setMin(min);
        }

        switch (valueType) {
            case ExcelProfilesFormat.REACTIVE_POWER:
                // Gen Q spreadSheet
                genElVal.setQ(value);
                break;
            case ExcelProfilesFormat.GEN_STATUS:
                Integer status = (value != null) ? value.intValue() : null;
                genElVal.setStatus(status);
                break;
            case ExcelProfilesFormat.VOLTAGE_MAGNITUDE:
                // Gen Vg spreadSheet
                genElVal.setVoltageMagnitude(value);
                break;
            default:
                // Gen P spreadSheet
                genElVal.setP(value);
        }

        elValMap.put(key, genElVal);
    }

    private void setGenElVal(
        Generator gen,
        String nominalVoltage,
        Long genIdOnSubstation,
        int hour,
        int min,
        Double value,
        Map<String, GenElVal> elValMap,
        String valueType
    ) {
        String key = gen.getId() + "_" + hour + "_" + min;

        GenElVal genElVal = elValMap.get(key);
        if (genElVal == null) {
            genElVal = new GenElVal();
            genElVal.generator(gen);
            genElVal.setHour(hour);
            genElVal.setMin(min);
            genElVal.setNominalVoltage(nominalVoltage);
            genElVal.setGenIdOnSubst(genIdOnSubstation);
        }

        switch (valueType) {
            case ExcelProfilesFormat.REACTIVE_POWER:
                // Gen Q spreadSheet
                genElVal.setQ(value);
                break;
            case ExcelProfilesFormat.GEN_STATUS:
                // Gen Status spreadSheet
                Integer status = (value != null) ? value.intValue() : null;
                genElVal.setStatus(status);
                break;
            case ExcelProfilesFormat.VOLTAGE_MAGNITUDE:
                // Gen Vg spreadSheet
                genElVal.setVoltageMagnitude(value);
                break;
            default:
                // Gen P spreadSheet
                genElVal.setP(value);
        }
        elValMap.put(key, genElVal);
    }

    private void saveBranchProfile(
        InputFileDTO inputFileDto,
        Network network,
        Integer mode,
        String season,
        String typicalDay,
        Double timeInterval,
        Map<String, BranchElVal> mapElValues
    ) {
        // -- save profile general info
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        BranchProfile profile = new BranchProfile();
        profile.setSeason(season);
        profile.setTypicalDay(typicalDay);
        profile.setMode(mode);
        profile.setTimeInterval(timeInterval);
        profile.setUploadDateTime(Instant.now());
        profile.setNetwork(network);
        profile.setInputFile(inputFile);
        BranchProfile profileSaved = branchProfileRepository.save(profile);
        LOGGER.info("New BranchProfile saved successfully: {} ", profileSaved);

        // -- save electrical values Status, etc.
        List<BranchElVal> values = new ArrayList<BranchElVal>();
        for (String key : mapElValues.keySet()) {
            BranchElVal val = mapElValues.get(key);
            val.setBranchProfile(profileSaved);
            BranchElVal newElVal = branchElValRepository.save(mapElValues.get(key));
            values.add(newElVal);
        }
        LOGGER.info("Number of BranchElVal saved: {}", values.size());
    }

    private void saveTransfProfile(
        InputFileDTO inputFileDto,
        Network network,
        Integer mode,
        String season,
        String typicalDay,
        Double timeInterval,
        Map<String, TransfElVal> mapElValues
    ) {
        // -- save profile general info
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        TransfProfile profile = new TransfProfile();
        profile.setSeason(season);
        profile.setTypicalDay(typicalDay);
        profile.setMode(mode);
        profile.setTimeInterval(timeInterval);
        profile.setUploadDateTime(Instant.now());
        profile.setNetwork(network);
        profile.setInputFile(inputFile);
        TransfProfile profileSaved = transfProfileRepository.save(profile);
        LOGGER.info("New TransfProfile saved successfully: {} ", profileSaved);

        // -- save electrical values tapRatio, Status, etc.
        List<TransfElVal> values = new ArrayList<TransfElVal>();
        for (String key : mapElValues.keySet()) {
            TransfElVal val = mapElValues.get(key);
            val.setTransfProfile(profileSaved);
            TransfElVal newElVal = transfElValRepository.save(mapElValues.get(key));
            values.add(newElVal);
        }
        LOGGER.info("Number of TransfElVal saved: {}", values.size());
    }

    private void saveLoadProfiles(
        InputFileDTO inputFileDto,
        Network network,
        Integer mode,
        String season,
        String typicalDay,
        Double timeInterval,
        Map<String, LoadElVal> mapElValues
    ) {
        // -- save profile general info
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        LoadProfile profile = new LoadProfile();
        profile.setSeason(season);
        profile.setTypicalDay(typicalDay);
        profile.setMode(mode);
        profile.setTimeInterval(timeInterval);
        profile.setUploadDateTime(Instant.now());
        profile.setNetwork(network);
        profile.setInputFile(inputFile);
        LoadProfile profileSaved = loadProfileRepository.save(profile);
        LOGGER.info("New LoadProfile saved successfully: {} ", profileSaved);

        // -- save electrical values P, Q, Status, etc.
        List<LoadElVal> values = new ArrayList<LoadElVal>();
        for (String key : mapElValues.keySet()) {
            LoadElVal val = mapElValues.get(key);
            val.setLoadProfile(profileSaved);
            LoadElVal newElVal = loadElValRepository.save(mapElValues.get(key));
            values.add(newElVal);
        }
        LOGGER.info("Number of LoadElVal saved: {}", values.size());
    }

    private void saveGenProfiles(
        InputFileDTO inputFileDto,
        Network network,
        Integer mode,
        String season,
        String typicalDay,
        Double timeInterval,
        Map<String, GenElVal> mapElValues
    ) {
        // -- save profile's general info
        InputFile inputFile = inputFileMapper.toEntity(inputFileDto);
        GenProfile profile = new GenProfile();
        profile.setSeason(season);
        profile.setTypicalDay(typicalDay);
        profile.setMode(mode);
        profile.setTimeInterval(timeInterval);
        profile.setUploadDateTime(Instant.now());
        profile.setNetwork(network);
        profile.setInputFile(inputFile);
        GenProfile profileSaved = genProfileRepository.save(profile);
        LOGGER.info("New GenProfile saved successfully: {} ", profileSaved);

        // -- save electrical's values P, Status, Vg etc.
        List<GenElVal> values = new ArrayList<GenElVal>();
        for (String key : mapElValues.keySet()) {
            GenElVal val = mapElValues.get(key);
            val.setGenProfile(profileSaved);
            GenElVal newElVal = genElValRepository.save(mapElValues.get(key));
            values.add(newElVal);
        }
        LOGGER.info("Number of GenElVal saved: {}", values.size());
    }
}
