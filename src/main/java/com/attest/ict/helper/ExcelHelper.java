package com.attest.ict.helper;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.NetworkRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ExcelHelper {

    // type of excel document
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // first sheet and related header
    static String[] FIRSTSHEETHEADERs = { "busi", "type", "Pd", "Qd", "Gs", "Bs", "area", "Vm", "Va", "baseKV", "zone", "Vmax", "Vmin" };
    static String FIRSTSHEET = "bus";

    // second sheet and related header
    static String[] SECONDSHEETHEADERs = {
        "fbus",
        "tbus",
        "r",
        "x",
        "b",
        "r0",
        "x0",
        "rateA",
        "rateB",
        "rateC",
        "ratio",
        "angle",
        "status",
        "angmin",
        "angmax",
        "length",
    };
    static String SECONDSHEET = "branch";

    // third sheed and related header
    static String[] THIRDSHEETHEADERs = {
        "bus",
        "Pg",
        "Qg",
        "Qmax",
        "Qmin",
        "Vg",
        "mBase",
        "status",
        "Pmax",
        "Pmin",
        "Pc1",
        "Pc2",
        "Qc1min",
        "Qc1max",
        "Qc2min",
        "Qc2max",
        "ramp_agc",
        "ramp_10",
        "ramp_30",
        "ramp_q",
        "apf",
    };
    static String THIRDSHEET = "gen";

    static NetworkRepository networkRepository;

    // to call network repository in a static function
    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initStaticNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    /* hasExcelFormat method
     * return true if the file is an xlsx file, else otherwise
     */
    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    /* appToExcel method
     * import data to an excel file (first three sheets)
     */
    public static ByteArrayInputStream appToExcel(List<Bus> buses, List<Branch> branches, List<Generator> generators) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet[] sheet = new Sheet[3];
            sheet[0] = workbook.createSheet(FIRSTSHEET);
            sheet[1] = workbook.createSheet(SECONDSHEET);
            sheet[2] = workbook.createSheet(THIRDSHEET);
            // Header
            Row headerRowFirstSheet = sheet[0].createRow(0);

            for (int colFirstSheet = 0; colFirstSheet < FIRSTSHEETHEADERs.length; colFirstSheet++) {
                Cell cell = headerRowFirstSheet.createCell(colFirstSheet);
                cell.setCellValue(FIRSTSHEETHEADERs[colFirstSheet]);
            }

            int rowFirstSheetIdx = 1;
            for (Bus bus : buses) {
                Row row = sheet[0].createRow(rowFirstSheetIdx++);

                row.createCell(0).setCellValue(bus.getId());
                row.createCell(1).setCellValue(bus.getType());
                row.createCell(2).setCellValue(bus.getActivePower());
                row.createCell(3).setCellValue(bus.getReactivePower());
                row.createCell(4).setCellValue(bus.getConductance());
                row.createCell(5).setCellValue(bus.getSusceptance());
                row.createCell(6).setCellValue(bus.getArea());
                row.createCell(7).setCellValue(bus.getVm());
                row.createCell(8).setCellValue(bus.getVa());
                row.createCell(9).setCellValue(bus.getBaseKv());
                row.createCell(10).setCellValue(bus.getZone());
                row.createCell(11).setCellValue(bus.getVmax());
                row.createCell(12).setCellValue(bus.getVmin());
            }

            // Header
            Row headerRowSecondSheet = sheet[1].createRow(0);

            for (int colSecondSheet = 0; colSecondSheet < SECONDSHEETHEADERs.length; colSecondSheet++) {
                Cell cell = headerRowSecondSheet.createCell(colSecondSheet);
                cell.setCellValue(SECONDSHEETHEADERs[colSecondSheet]);
            }

            int rowSecondSheetIdx = 1;
            for (Branch branch : branches) {
                Row row = sheet[1].createRow(rowSecondSheetIdx++);

                row.createCell(0).setCellValue(branch.getFbus());
                row.createCell(1).setCellValue(branch.getTbus());
                row.createCell(2).setCellValue(branch.getR());
                row.createCell(3).setCellValue(branch.getX());
                row.createCell(4).setCellValue(branch.getB());
                row.createCell(5).setCellValue(branch.getRatea());
                row.createCell(6).setCellValue(branch.getRateb());
                row.createCell(7).setCellValue(branch.getRatec());
                row.createCell(8).setCellValue(branch.getTapRatio());
                row.createCell(9).setCellValue(branch.getAngle());
                row.createCell(10).setCellValue(branch.getStatus());
                row.createCell(11).setCellValue(branch.getAngmin());
                row.createCell(112).setCellValue(branch.getAngmax());
            }

            // Header
            Row headerRowThirdSheet = sheet[2].createRow(0);

            for (int colThirdSheet = 0; colThirdSheet < THIRDSHEETHEADERs.length; colThirdSheet++) {
                Cell cell = headerRowThirdSheet.createCell(colThirdSheet);
                cell.setCellValue(THIRDSHEETHEADERs[colThirdSheet]);
            }

            int rowThirdSheetIdx = 1;
            for (Generator generator : generators) {
                Row row = sheet[2].createRow(rowThirdSheetIdx++);

                row.createCell(0).setCellValue(generator.getBusNum());
                row.createCell(1).setCellValue(generator.getPg());
                row.createCell(2).setCellValue(generator.getQg());
                row.createCell(3).setCellValue(generator.getQmax());
                row.createCell(4).setCellValue(generator.getQmin());
                row.createCell(5).setCellValue(generator.getVg());
                row.createCell(6).setCellValue(generator.getmBase());
                row.createCell(7).setCellValue(generator.getStatus());
                row.createCell(8).setCellValue(generator.getPmax());
                row.createCell(9).setCellValue(generator.getPmin());
                row.createCell(10).setCellValue(generator.getPc1());
                row.createCell(11).setCellValue(generator.getPc2());
                row.createCell(12).setCellValue(generator.getQc1min());
                row.createCell(13).setCellValue(generator.getQc1max());
                row.createCell(14).setCellValue(generator.getQc2min());
                row.createCell(15).setCellValue(generator.getQc2max());
                row.createCell(16).setCellValue(generator.getRampAgc());
                row.createCell(17).setCellValue(generator.getRamp10());
                row.createCell(18).setCellValue(generator.getRamp30());
                row.createCell(19).setCellValue(generator.getRampQ());
                row.createCell(20).setCellValue(generator.getApf());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    /* excelToBuses method
     * parse an excel file and return a list of bus
     */
    public static List<Bus> excelToBuses(InputStream is, String networkName, Long networkId) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
            }

            Sheet sheet = workbook.getSheet(FIRSTSHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Bus> busList = new ArrayList<Bus>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Bus bus = new Bus();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            bus.setBusNum((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            bus.setType((int) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            bus.setActivePower(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            bus.setReactivePower(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            bus.setConductance(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            bus.setSusceptance(currentCell.getNumericCellValue());
                            break;
                        case 6:
                            bus.setArea((long) currentCell.getNumericCellValue());
                            break;
                        case 7:
                            bus.setVm(currentCell.getNumericCellValue());
                            break;
                        case 8:
                            bus.setVa(currentCell.getNumericCellValue());
                            break;
                        case 9:
                            bus.setBaseKv(currentCell.getNumericCellValue());
                            break;
                        case 10:
                            bus.setZone((long) currentCell.getNumericCellValue());
                            break;
                        case 11:
                            bus.setVmax(currentCell.getNumericCellValue());
                            break;
                        case 12:
                            bus.setVmin(currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                if (networkId == null) {
                    bus.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    bus.setNetwork(networkRepository.findById(networkId).get());
                }

                busList.add(bus);
            }

            workbook.close();

            return busList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    /* excelToBranches method
     * parse an excel file and return a list of branches
     */
    public static List<Branch> excelToBranches(InputStream is, String networkName, Long networkId) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SECONDSHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Branch> branchList = new ArrayList<Branch>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Branch branch = new Branch();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        /*case 0:
                            branch.setBranch((long) currentCell.getNumericCellValue());
                            break;*/

                        case 1:
                            branch.setFbus((long) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            branch.setTbus((long) currentCell.getNumericCellValue());
                            break;
                        case 3:
                            branch.setR(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            branch.setX(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            branch.setB(currentCell.getNumericCellValue());
                            break;
                        case 6:
                            branch.setRatea((double) currentCell.getNumericCellValue());
                            break;
                        case 7:
                            branch.setRateb((double) currentCell.getNumericCellValue());
                            break;
                        case 8:
                            branch.setRatec((double) currentCell.getNumericCellValue());
                            break;
                        case 9:
                            branch.setTapRatio((double) currentCell.getNumericCellValue());
                            break;
                        case 10:
                            branch.setAngle((double) currentCell.getNumericCellValue());
                            break;
                        case 11:
                            branch.setStatus((int) currentCell.getNumericCellValue());
                            break;
                        case 12:
                            branch.setAngmin((int) currentCell.getNumericCellValue());
                            break;
                        case 13:
                            branch.setAngmax((int) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }
                if (networkId == null) {
                    branch.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    branch.setNetwork(networkRepository.findById(networkId).get());
                }

                branchList.add(branch);
            }

            workbook.close();

            return branchList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    /* excelToGenerators method
     * parse an excel file and return a list of generators
     */
    public static List<Generator> excelToGenerators(InputStream is, String networkName, Long networkId) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(THIRDSHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Generator> generatorList = new ArrayList<Generator>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Generator generator = new Generator();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            generator.setBusNum((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            generator.setPg(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            generator.setQg(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            generator.setQmax(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            generator.setQmin(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            generator.setVg(currentCell.getNumericCellValue());
                            break;
                        case 6:
                            generator.setmBase(currentCell.getNumericCellValue());
                            break;
                        case 7:
                            generator.setStatus((int) currentCell.getNumericCellValue());
                            break;
                        case 8:
                            generator.setPmax(currentCell.getNumericCellValue());
                            break;
                        case 9:
                            generator.setPmin(currentCell.getNumericCellValue());
                            break;
                        case 10:
                            generator.setPc1(currentCell.getNumericCellValue());
                            break;
                        case 11:
                            generator.setPc2(currentCell.getNumericCellValue());
                            break;
                        case 12:
                            generator.setQc1min(currentCell.getNumericCellValue());
                            break;
                        case 13:
                            generator.setQc1max(currentCell.getNumericCellValue());
                            break;
                        case 14:
                            generator.setQc2min(currentCell.getNumericCellValue());
                            break;
                        case 15:
                            generator.setQc2max(currentCell.getNumericCellValue());
                            break;
                        case 16:
                            generator.setRampAgc(currentCell.getNumericCellValue());
                            break;
                        case 17:
                            generator.setRamp10(currentCell.getNumericCellValue());
                            break;
                        case 18:
                            generator.setRamp30(currentCell.getNumericCellValue());
                            break;
                        case 19:
                            generator.setRampQ(currentCell.getNumericCellValue());
                            break;
                        case 20:
                            generator.setApf((long) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (networkId == null) {
                    generator.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    generator.setNetwork(networkRepository.findById(networkId).get());
                }
                generatorList.add(generator);
            }

            workbook.close();

            return generatorList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    /* excelToNetworks method
     * parse an excel file and return a list of networks
     */
    public static List<Network> excelToNetworks(Network newNetwork, Instant instant) {
        //Date date = new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime();
        LocalDateTime netDateTime = LocalDate.of(2020, 2, 11).atStartOfDay(); // year, month, day
        Instant netInstant = netDateTime.atZone(ZoneId.systemDefault()).toInstant();

        List<Network> networkList = new ArrayList<Network>();
        Network network = new Network();
        network.setNetworkDate(netInstant);

        network.setName(newNetwork.getName());
        network.setUpdateDateTime(instant);
        networkList.add(network);

        return networkList;
    }
}
