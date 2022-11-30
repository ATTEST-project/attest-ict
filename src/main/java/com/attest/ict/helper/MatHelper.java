package com.attest.ict.helper;

import static com.attest.ict.helper.ExcelHelper.networkRepository;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.Matrix;

public class MatHelper {

    // type of matlab files
    public static String TYPE = "application/octet-stream";

    public static boolean hasMatlabFormat(MultipartFile file) {
        // check if the file has .mat format
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    /* matToBus method
     * parse a matlab file and return a list of bus
     */
    public static List<Bus> matToBus(File convFile, String networkName, Long networkId) throws IOException {
        try (Mat5File matFile = Mat5.readFromFile(convFile)) {
            // index shall be 0 for the first workspace
            Matrix matlabMatrixBus = matFile.getStruct(0).getMatrix("bus");

            List<Bus> busList = new ArrayList<Bus>();

            for (int i = 0; i < matlabMatrixBus.getNumRows(); i++) {
                Bus bus = new Bus();

                for (int j = 0; j < matlabMatrixBus.getNumCols(); j++) {
                    switch (j) {
                        case 0:
                            bus.setBusNum(matlabMatrixBus.getLong(i, j));
                            break;
                        case 1:
                            bus.setType(matlabMatrixBus.getInt(i, j));
                            break;
                        case 2:
                            bus.setActivePower(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 3:
                            bus.setReactivePower(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 4:
                            bus.setConductance(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 5:
                            bus.setSusceptance(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 6:
                            bus.setArea(matlabMatrixBus.getLong(i, j));
                            break;
                        case 7:
                            bus.setVm(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 8:
                            bus.setVa(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 9:
                            bus.setBaseKv(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 10:
                            bus.setZone(matlabMatrixBus.getLong(i, j));
                            break;
                        case 11:
                            bus.setVmax(matlabMatrixBus.getDouble(i, j));
                            break;
                        case 12:
                            bus.setVmin(matlabMatrixBus.getDouble(i, j));
                            break;
                        default:
                            break;
                    }
                }
                if (networkId == null) {
                    bus.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    bus.setNetwork(networkRepository.findById(networkId).get());
                }
                busList.add(bus);
            }
            return busList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Matlab file: " + e.getMessage());
        }
    }

    /* matToBranch method
     * parse a matlab file and return a list of branches
     */
    public static List<Branch> matToBranch(File convFile, String networkName, Long networkId) throws IOException {
        try (Mat5File matFile = Mat5.readFromFile(convFile)) {
            // index shall be 0 for the first workspace
            Matrix matlabMatrixBranch = matFile.getStruct(0).getMatrix("branch");
            List<Branch> branchList = new ArrayList<Branch>();

            for (int i = 0; i < matlabMatrixBranch.getNumRows(); i++) {
                Branch branch = new Branch();

                for (int j = 0; j < matlabMatrixBranch.getNumCols(); j++) {
                    switch (j) {
                        case 0:
                            branch.setFbus(matlabMatrixBranch.getLong(i, j));
                            break;
                        case 1:
                            branch.setTbus(matlabMatrixBranch.getLong(i, j));
                            break;
                        case 2:
                            branch.setR(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 3:
                            branch.setX(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 4:
                            branch.setB(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 5:
                            branch.setRatea(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 6:
                            branch.setRateb(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 7:
                            branch.setRatec(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 8:
                            branch.setTapRatio(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 9:
                            branch.setAngle(matlabMatrixBranch.getDouble(i, j));
                            break;
                        case 10:
                            branch.setStatus(matlabMatrixBranch.getInt(i, j));
                            break;
                        case 11:
                            branch.setAngmin(matlabMatrixBranch.getInt(i, j));
                            break;
                        case 12:
                            branch.setAngmax(matlabMatrixBranch.getInt(i, j));
                            break;
                    }
                }
                if (networkId == null) {
                    branch.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    branch.setNetwork(networkRepository.findById(networkId).get());
                }
                branchList.add(branch);
            }
            return branchList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Matlab file: " + e.getMessage());
        }
    }

    /* matToGenerator method
     * parse a matlab file and return a list of generators
     */
    public static List<Generator> matToGenerator(File convFile, String networkName, Long networkId) throws IOException {
        try (Mat5File matFile = Mat5.readFromFile(convFile)) {
            // index shall be 0 for the first workspace
            Matrix matlabMatrixGenerator = matFile.getStruct(0).getMatrix("gen");
            List<Generator> generatorList = new ArrayList<Generator>();

            for (int i = 0; i < matlabMatrixGenerator.getNumRows(); i++) {
                Generator generator = new Generator();

                for (int j = 0; j < matlabMatrixGenerator.getNumCols(); j++) {
                    switch (j) {
                        case 0:
                            generator.setBusNum(matlabMatrixGenerator.getLong(i, j));
                            break;
                        case 1:
                            generator.setPg(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 2:
                            generator.setQg(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 3:
                            generator.setQmax(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 4:
                            generator.setQmin(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 5:
                            generator.setVg(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 6:
                            generator.setmBase(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 7:
                            generator.setStatus(matlabMatrixGenerator.getInt(i, j));
                            break;
                        case 8:
                            generator.setPmax(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 9:
                            generator.setPmin(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 10:
                            generator.setPc1(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 11:
                            generator.setPc2(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 12:
                            generator.setQc1min(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 13:
                            generator.setQc1max(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 14:
                            generator.setQc2min(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 15:
                            generator.setQc2max(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 16:
                            generator.setRampAgc(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 17:
                            generator.setRamp10(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 18:
                            generator.setRamp30(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 19:
                            generator.setRampQ(matlabMatrixGenerator.getDouble(i, j));
                            break;
                        case 20:
                            generator.setApf(matlabMatrixGenerator.getLong(i, j));
                            break;
                    }
                }
                if (networkId == null) {
                    generator.setNetwork(networkRepository.findByName(networkName).get());
                } else {
                    generator.setNetwork(networkRepository.findById(networkId).get());
                }
                generatorList.add(generator);
            }

            return generatorList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Matlab file: " + e.getMessage());
        }
    }
}
