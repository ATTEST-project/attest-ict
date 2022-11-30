package com.attest.ict.service.impl;

import com.attest.ict.chart.utils.T41ResultsChartsDataSet;
import com.attest.ict.chart.utils.T44ResultsChartsDataSet;
import com.attest.ict.chart.utils.TSGResultsChartsDataSet;
import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.model.FlexibleOptionWithContin;
import com.attest.ict.helper.excel.reader.T41ResultsReader;
import com.attest.ict.helper.excel.reader.T44FileOutputFormat;
import com.attest.ict.helper.excel.reader.T44ResultsReader;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.reader.OdsTSGResultsReader;
import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.service.ToolWp4ShowResultsService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T41ResultsDTO;
import com.attest.ict.service.dto.custom.T44ResultsDTO;
import com.attest.ict.service.dto.custom.TSGResultsDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ToolWp4ShowResultsServiceImpl implements ToolWp4ShowResultsService {

    private final Logger log = LoggerFactory.getLogger(ToolWp4ShowResultsServiceImpl.class);

    private String attestToolsDir;

    private String toolsPathSimulation;

    public ToolWp4ShowResultsServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);

        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    /**
     *
     */
    public Map<String, Integer> t44TableResults(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        T44ResultsReader reader = new T44ResultsReader();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        Map<String, List<FlexibleOptionWithContin>> mapAllExcelData = new HashMap<String, List<FlexibleOptionWithContin>>();
        int numFileRead = reader.readAllFileInDir(outputDir, mapAllExcelData);

        if (numFileRead == 0) {
            // Tool doesn't produce output files
            return Collections.EMPTY_MAP;
        }

        Map<String, Integer> nScNContingMap = reader.getNContingNSc(mapAllExcelData);
        if (nScNContingMap.isEmpty()) {
            nScNContingMap = new HashMap<String, Integer>();
        }

        // -- Verify if Network_Normal.xlsx is present in fileSystem
        String extension = T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(2);
        if (reader.isFilePresent(outputDir, T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(2))) {
            nScNContingMap.put(extension, 1);
        }

        // -- Verify if Network_OPF.xlsx is present in filesystem
        extension = T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(1);
        if (reader.isFilePresent(outputDir, T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(1))) {
            nScNContingMap.put(extension, 1);
        }

        Map<String, Integer> newMap = ConverterUtils.mapReplaceKeyChar(nScNContingMap, " ", "_", true);
        return newMap;
    }

    @Override
    public T44ResultsDTO t44ChartsByNContingAndNsc(NetworkDTO networkDto, ToolDTO toolDto, String uuid, Integer nConting, Integer nSc)
        throws FileNotFoundException {
        T44ResultsReader reader = new T44ResultsReader();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        Map<String, List<FlexibleOptionWithContin>> mapAllExcelData = new HashMap<String, List<FlexibleOptionWithContin>>();
        // Read data from <Network>_PContin_*.xlsx
        int numPConingFileRead = reader.readFileInDirByType(outputDir, mapAllExcelData, T44FileOutputFormat.P_CONTIN_SUB_STR);

        // Reading data from excel file: <Network>_Costs.xlsx (SpreadSheet: Sheet1)
        int numCostFileRead = reader.readFileInDirByType(outputDir, mapAllExcelData, T44FileOutputFormat.COSTS_SUB_STR);

        if (numPConingFileRead + numCostFileRead == 0) {
            // Tool doesn't produce output files
            T44ResultsDTO emptyResult = new T44ResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
            emptyResult.setFlexCosts(Collections.EMPTY_LIST);
            return emptyResult;
        }

        Map<String, List<FlexibleOptionWithContin>> mapFlexibleOptions = new HashMap<String, List<FlexibleOptionWithContin>>();

        List<FlexibleOptionWithContin> costFromExcelCosts = mapAllExcelData.get(T44FileOutputFormat.SHEETS_NAME_COST.get(0));
        mapFlexibleOptions.put(T44FileOutputFormat.SHEETS_NAME_COST.get(0), costFromExcelCosts);

        Map<String, List<FlexibleOptionWithContin>> dataByNscAndNConting = reader.getDataByNContingAndNsc(nConting, nSc, mapAllExcelData);
        if (!dataByNscAndNConting.isEmpty()) {
            mapFlexibleOptions.putAll(dataByNscAndNConting);
        }

        T44ResultsChartsDataSet resultsChartDataSet = new T44ResultsChartsDataSet();
        return resultsChartDataSet.prepareChartsDataSet(mapFlexibleOptions);
    }

    // @type possible values are Normal or OPF
    @Override
    public T44ResultsDTO t44ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP4/T44/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        log.info("Reading T44 output files and prepare JSON file for charts visualization");
        T44ResultsReader reader = new T44ResultsReader();
        Map<String, List<FlexibleOptionWithContin>> mapAllExcelData = new HashMap<String, List<FlexibleOptionWithContin>>();
        int numFileRead = reader.readFileInDirByType(outputDir, mapAllExcelData, type);
        if (numFileRead == 0) {
            // Tool doesn't produce output files
            T44ResultsDTO emptyResult = new T44ResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
            emptyResult.setFlexCosts(Collections.EMPTY_LIST);
            return emptyResult;
        }

        T44ResultsChartsDataSet resultsChartDataSet = new T44ResultsChartsDataSet();
        return resultsChartDataSet.prepareChartsDataSet(mapAllExcelData);
    }

    @Override
    public T41ResultsDTO t41Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid)
        throws FileNotFoundException, ExcelReaderFileException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        log.info("Reading T41 output file and prepare JSON file for charts visualization");
        T41ResultsReader reader = new T41ResultsReader();
        Map<String, List<FlexibleOption>> mapDataForSheet = new HashMap<String, List<FlexibleOption>>();
        mapDataForSheet = reader.readFileInDir(outputDir);

        // outputDir doesn't contain file
        if (mapDataForSheet.isEmpty()) {
            // Tool doesn't produce output files
            T41ResultsDTO emptyResult = new T41ResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
            return emptyResult;
        }

        T41ResultsChartsDataSet resultsChartDataSet = new T41ResultsChartsDataSet();
        return resultsChartDataSet.t41PrepareChartsDataSet(mapDataForSheet);
    }

    @Override
    public TSGResultsDTO windAndPVCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid)
        throws FileNotFoundException, OdsReaderFileException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // eg: /ATSIM/WP4/TSG/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);

        // eg: /ATSIM/WP4/TSG/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        log.info("Reading WindPVScenarioGenTool's output file and prepare JSON file for charts visualization");
        OdsTSGResultsReader reader = new OdsTSGResultsReader();
        Map<String, List<ScenarioValues>> mapDataForSheet = new HashMap<String, List<ScenarioValues>>();
        mapDataForSheet = reader.readFileInDir(outputDir);

        TSGResultsChartsDataSet resultsChartDataSet = new TSGResultsChartsDataSet();
        return resultsChartDataSet.tsgPrepareChartsDataSet(mapDataForSheet);
    }

    /** @Override
    public File getOutputFileOld(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found");
        }
        File[] files = dir.listFiles();

        //tool doesn't produce any file
        if (files.length == 0) {
            return null;
        }

        if (files.length == 1) {
            return files[0];
        } else {
            //ZIP  all output files
            String archiveName = toolDto.getNum() + "_" + uuid + ".zip";
            List<File> fileList = Arrays.asList(files);
            return FileUtils.zip(fileList, archiveName);
        }
    }*/

    @Override
    public File[] getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/T41/1bebbcae-8157-4842-9b0b-1303dbc48f2c/output_data
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found");
        }
        File[] files = dir.listFiles();

        return files;
    }
}
