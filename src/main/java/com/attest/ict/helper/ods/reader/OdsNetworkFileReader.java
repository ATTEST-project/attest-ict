package com.attest.ict.helper.ods.reader;

import com.attest.ict.domain.BaseMVA;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.reader.model.Load;
import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class OdsNetworkFileReader extends OdsFileReader {

    public final Logger log = LoggerFactory.getLogger(OdsNetworkFileReader.class);

    public OdsNetworkFileReader(MultipartFile file) {
        super(file);
    }

    public OdsNetworkFileReader(String strPath) {
        super(strPath);
    }

    public OdsNetworkFileReader(File relativePath) {
        super(relativePath);
    }

    public List<Sheet> parseOdsNetworkFile() throws Exception {
        List<Sheet> sheetsForTools = new ArrayList<Sheet>();
        try {
            log.debug("Start reading file: {} ", this.odsFileName);
            List<Sheet> sheets = this.spreadSheet.getSheets();
            for (Sheet sheet : sheets) {
                String name = sheet.getName();
                if (T41FileInputFormat.netwrokSheetMap.containsKey(name)) {
                    sheetsForTools.add(sheet);
                } else {
                    log.debug("sheet: {} not required, will be skip ", name);
                }
            }
            return sheetsForTools;
        } catch (OdsReaderFileException orfe) {
            String errMsg = "Error parsing ODS file: " + this.odsFileName + " " + orfe.getMessage();
            log.error(errMsg);
            return new ArrayList<Sheet>();
        }
    }

    /**
     * @param sheet with bus data
     * @return list of bus value
     */
    public List<Bus> parseSheetBuses(Sheet sheet, Network network) {
        double defaultVal = 0d;
        List<Bus> buses = new ArrayList<Bus>();
        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // first row contains header
            // -- rows
            for (int r = 1; r < rows; r++) {
                // columns
                Bus bus = new Bus();
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    if (hasContent(rangeCell)) {
                        Double valueObj = (Double) rangeCell.getValue();
                        // "bus_i", "type", "area", "Vm", "Va", "baseKV", "zone", "Vmax", "Vmin
                        switch (c) {
                            case 0:
                                // bus_i
                                bus.setBusNum(valueObj.longValue());
                                break;
                            case 1:
                                // type
                                bus.setType(valueObj.intValue());
                                break;
                            case 2:
                                // area
                                bus.setArea(valueObj.longValue());
                                break;
                            case 3:
                                // vm
                                bus.setVm(valueObj);
                                break;
                            case 4:
                                // Va
                                bus.setVa(valueObj);
                                break;
                            case 5:
                                // baseKv
                                bus.setBaseKv(valueObj);
                                break;
                            case 6:
                                // zone
                                bus.setZone(valueObj.longValue());
                                break;
                            case 7:
                                // Vmax
                                bus.setVmax(valueObj);
                                break;
                            case 8:
                                // Vmin
                                bus.setVmin(valueObj);
                                break;
                        }
                    }
                }

                if (bus.getBusNum() != null) {
                    bus.setNetwork(network);
                    // set default values for value present in 'load' SpreadSheet
                    bus.setActivePower(defaultVal);
                    bus.setReactivePower(defaultVal);
                    bus.setConductance(defaultVal);
                    bus.setSusceptance(defaultVal);
                    buses.add(bus);
                }
            } // end for rows
        }
        return buses;
    }

    /**
     * @param sheet with branch(lines) data
     * @return list of branch value
     */
    public List<Branch> parseSheetLines(Sheet sheet, Network network) {
        List<Branch> branches = new ArrayList<Branch>();
        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // first row contains header
            // -- rows
            for (int r = 1; r < rows; r++) {
                // -- columns
                Branch branch = new Branch();
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    // ODS delivered with T41 contains null value in all cell , they must be filter
                    if (hasContent(rangeCell)) {
                        Double valueObj = (Double) rangeCell.getValue();
                        // fbus tbus r x b rateA rateB rateC ratio angle status angmin angmax
                        switch (c) {
                            case 0:
                                // fbus
                                branch.setFbus(valueObj.longValue());
                                break;
                            case 1:
                                // tbus
                                branch.setTbus(valueObj.longValue());
                                break;
                            case 2:
                                // r
                                branch.setR(valueObj);
                                break;
                            case 3:
                                // x
                                branch.setX(valueObj);
                                break;
                            case 4:
                                // v
                                branch.setB(valueObj);
                                break;
                            case 5:
                                // rateA
                                branch.setRatea(valueObj);
                                break;
                            case 6:
                                // rateB
                                branch.setRateb(valueObj);
                                break;
                            case 7:
                                // rateC
                                branch.setRatec(valueObj);
                                break;
                            case 8:
                                // ratio
                                branch.setTapRatio(valueObj);
                                break;
                            case 9:
                                // angle
                                branch.setAngle(valueObj);
                                break;
                            case 10:
                                // status
                                branch.setStatus(valueObj.intValue());
                                break;
                            case 11:
                                // angmin
                                branch.setAngmin(valueObj.intValue());
                                break;
                            case 12:
                                // angmax
                                branch.setAngmax(valueObj.intValue());
                                break;
                        }
                    }
                }
                if (branch.getFbus() != null) {
                    branch.setNetwork(network);
                    branches.add(branch);
                } // end for rows
            }
        }
        return branches;
    }

    /**
     * @param sheet with loads data
     * @return list of load value
     */
    public List<Load> parseSheetLoads(Sheet sheet) {
        List<Load> loads = new ArrayList<Load>();
        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // first row contains header
            // -- rows
            for (int r = 1; r < rows; r++) {
                // columns
                Load load = new Load();
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    if (hasContent(rangeCell)) {
                        Double valueObj = (Double) rangeCell.getValue();
                        // bus_i Pd Qd Gs Bs
                        switch (c) {
                            case 0:
                                // bus_i
                                load.setBusNum(valueObj.longValue());
                                break;
                            case 1:
                                // Pg
                                load.setPd(valueObj);
                                break;
                            case 2:
                                // Qg
                                load.setQd(valueObj);
                                break;
                            case 3:
                                // Qmax
                                load.setGs(valueObj);
                                break;
                            case 4:
                                // Qmin
                                load.setBs(valueObj);
                                break;
                        }
                    }
                } // -- end cols

                if (load.getBusNum() != null) {
                    loads.add(load);
                }
            } // -- end rows
        }

        return loads;
    }

    /**
     * @param sheet with generator data
     * @return list of generator object
     */
    public List<Generator> parseSheetGens(Sheet sheet, Network network) {
        List<Generator> gens = new ArrayList<Generator>();

        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // first row contains header
            // -- rows
            for (int r = 1; r < rows; r++) {
                // columns
                Generator gen = new Generator();
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    if (hasContent(rangeCell)) {
                        Double valueObj = (Double) rangeCell.getValue();
                        // bus_i Pg Qg Qmax Qmin Vg mBase status Pmax Pmin Pc1 Pc2 Qc1min Qc1max Qc2min
                        // Qc2max ramp_agc ramp_10 ramp_30 ramp_q apf
                        switch (c) {
                            case 0:
                                // bus_i
                                gen.setBusNum(valueObj.longValue());
                                break;
                            case 1:
                                // Pg
                                gen.setPg(valueObj);
                                break;
                            case 2:
                                // Qg
                                gen.setQg(valueObj);
                                break;
                            case 3:
                                // Qmax
                                gen.setQmax(valueObj);
                                break;
                            case 4:
                                // Qmin
                                gen.setQmin(valueObj);
                                break;
                            case 5:
                                // Vg
                                gen.setVg(valueObj);
                                break;
                            case 6:
                                // MBase
                                gen.setmBase(valueObj);
                                break;
                            case 7:
                                // status
                                gen.setStatus(valueObj.intValue());
                                break;
                            case 8:
                                // Pmax
                                gen.setPmax(valueObj);
                                break;
                            case 9:
                                // Pmin
                                gen.setPmin(valueObj);
                                break;
                            case 10:
                                // Pc1
                                gen.setPc1(valueObj);
                                break;
                            case 11:
                                // Pc2
                                gen.setPc2(valueObj);
                                break;
                            case 12:
                                // Qc1min
                                gen.setQc1min(valueObj);
                                break;
                            case 13:
                                // Qc1max
                                gen.setQc1max(valueObj);
                                break;
                            case 14:
                                // Qc2min
                                gen.setQc2min(valueObj);
                                break;
                            case 15:
                                // Qc2Max
                                gen.setQc2max(valueObj);
                                break;
                            case 16:
                                // ramp_agc
                                gen.setRampAgc(valueObj);
                                break;
                            case 17:
                                // ramp_10
                                gen.setRamp10(valueObj);
                                break;
                            case 18:
                                // ramp_30
                                gen.setRamp30(valueObj);
                                break;
                            case 19:
                                // ramp_q
                                gen.setRampQ(valueObj);
                                break;
                            case 20:
                                // apf
                                gen.setApf(valueObj.longValue());
                                break;
                        }
                    }
                }

                if (gen.getBusNum() != null) {
                    gen.setNetwork(network);
                    gens.add(gen);
                }
            } // end for rows
        }

        return gens;
    }

    /**
     * @param sheet BaseMVA
     * @param network
     * @return list of BaseMVA object
     */
    public List<BaseMVA> parseSheetBaseMVA(Sheet sheet, Network network) {
        List<BaseMVA> baseMVAList = new ArrayList<BaseMVA>();
        Range range = sheet.getDataRange();
        if (this.hasContent(range)) {
            int rows = range.getNumRows();
            int cols = range.getNumColumns();
            // first row contains header - skips
            // -- rows
            for (int r = 1; r < rows; r++) {
                // columns
                BaseMVA b = new BaseMVA();
                for (int c = 0; c < cols; c++) {
                    Range rangeCell = sheet.getRange(r, c);
                    if (hasContent(rangeCell)) {
                        Double valueObj = (Double) rangeCell.getValue();
                        b.setBaseMva(valueObj);
                    }
                }
                if (b.getBaseMva() != null) {
                    b.setNetwork(network);
                    baseMVAList.add(b);
                }
            } // end for rows
        }

        return baseMVAList;
    }

    public static void main(String args[]) {
        boolean testNetFile = true;
        if (testNetFile) {
            // String netFileName =                "C:\\SVILUPPO\\ATTEST\\GIT-LAB\\BR_JHI_BACKEND_NEW\\jhipster-attest\\src\\test\\resources\\ods_file\\uk_dx_01_2020.ods";
            // String ptNetFileName =
            // "C:\\SVILUPPO\\ATTEST\\GIT-LAB\\BR_JHI_BACKEND\\jhipster-attest\\src\\test\\resources\\ods_file\\pt_dx_01_2020.ods";

            String netFileName = "C:\\temp\\T41V2_Check\\BaseCase.ods";
            OdsNetworkFileReader reader = new OdsNetworkFileReader(netFileName);
            try {
                File fileOds = new File(netFileName);
                List<Sheet> sheets = reader.parseOdsNetworkFile();
                for (Sheet sheet : sheets) {
                    String name = sheet.getName();
                    System.out.println("Reading... sheet: " + name);
                    if (name.equals(T41FileInputFormat.networkSheetHeaders.get(0))) {
                        List<Bus> buses = reader.parseSheetBuses(sheet, null);
                        for (Bus bus : buses) {
                            System.out.println("  Bus: " + bus.toString());
                        }
                    }

                    if (name.equals(T41FileInputFormat.networkSheetHeaders.get(1))) {
                        List<Branch> branches = reader.parseSheetLines(sheet, null);
                        for (Branch b : branches) {
                            System.out.println("  Line/Branch: " + b.toString());
                        }
                    }

                    if (name.equals(T41FileInputFormat.networkSheetHeaders.get(2))) {
                        List<Load> loads = reader.parseSheetLoads(sheet);
                        for (Load l : loads) {
                            System.out.println("  Load: " + l.toString());
                        }
                    }

                    if (name.equals(T41FileInputFormat.networkSheetHeaders.get(3))) {
                        List<Generator> gens = reader.parseSheetGens(sheet, null);
                        for (Generator g : gens) {
                            System.out.println("  Generator: " + g.toString());
                        }
                    }
                    if (name.equals(T41FileInputFormat.networkSheetHeaders.get(4))) {
                        List<BaseMVA> baseMvas = reader.parseSheetBaseMVA(sheet, null);
                        for (BaseMVA b : baseMvas) {
                            System.out.println(" BaseMVA: " + b.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String fileName = "/SVILUPPO/ATTEST/GIT-LAB/BR_JHI_BACKEND-NEW/jhipster-attest/src/test/resources/ods_file/es_dx_01_2020.ods";

            OdsNetworkFileReader reader = new OdsNetworkFileReader(fileName);
            try {
                File odsFile = new File(fileName);
                //SpreadSheet spread = new SpreadSheet(odsFile);
                SpreadSheet spread = reader.spreadSheet;
                System.out.println("Sheets Number: " + spread.getNumSheets());
                List<Sheet> sheets = spread.getSheets();
                for (Sheet sheet : sheets) {
                    String name = sheet.getName();
                    System.out.println("Reading Sheet: " + name);
                    Range range = sheet.getDataRange();
                    System.out.println(
                        "Reading Sheet: " + name + " Num Rows: " + range.getNumRows() + " Num Cols: " + range.getNumColumns()
                    );
                    for (int r = 1; r < range.getNumRows(); r++) {
                        for (int c = 0; c < range.getNumColumns(); c++) {
                            Range cellRange = sheet.getRange(r, c);
                            if (reader.hasContent(cellRange)) {
                                Object value = cellRange.getValue();
                                System.out.println(" cell [" + r + "," + c + "]  value:" + value + " Class: " + value.getClass());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
