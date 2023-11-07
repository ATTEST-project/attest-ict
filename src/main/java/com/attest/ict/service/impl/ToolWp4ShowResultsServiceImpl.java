package com.attest.ict.service.impl;

import com.attest.ict.chart.utils.*;
import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.excel.model.FlexibleOption;
import com.attest.ict.helper.excel.model.FlexibleOptionWithContin;
import com.attest.ict.helper.excel.reader.*;
import com.attest.ict.helper.excel.util.ExcelFileFormat;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.helper.ods.reader.OdsTSGResultsReader;
import com.attest.ict.helper.ods.reader.model.ScenarioValues;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.attest.ict.service.ToolWp4ShowResultsService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.*;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ToolWp4ShowResultsServiceImpl implements ToolWp4ShowResultsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ToolWp4ShowResultsServiceImpl.class);

    private final String attestToolsDir;

    private final String toolsPathSimulation;

    public ToolWp4ShowResultsServiceImpl(ToolsConfiguration toolsConfig) {
        //ATTEST/tools: Tool's installation PATH
        this.attestToolsDir = toolsConfig.getPath();
        LOGGER.debug("ToolWp4ShowResultsServiceImpl() - AttestToolsDir {}", attestToolsDir);

        //ATSIM: Simulation Working Dir
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        LOGGER.debug("ToolWp4ShowResultsServiceImpl() - toolsPathSimulation {}", toolsPathSimulation);
    }

    //--- Scenario Gen Tool Wind and PV

    @Override
    public TSGResultsDTO windAndPVCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid)
        throws FileNotFoundException, OdsReaderFileException {
        Map<String, List<ScenarioValues>> mapDataForSheet = new HashMap<String, List<ScenarioValues>>();
        File scenarioResultsFile = this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, TSGFileOutputFormat.toolOutputFile);
        OdsTSGResultsReader reader = new OdsTSGResultsReader(scenarioResultsFile);
        mapDataForSheet = reader.parseOdsTSGResults();

        TSGResultsChartsDataSet resultsChartDataSet = new TSGResultsChartsDataSet();
        return resultsChartDataSet.tsgPrepareChartsDataSet(mapDataForSheet);
    }

    //--- T41 ---
    @Override
    public T41ResultsPagesDTO t41PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception {
        // --- search simulation's produced by Tool during simulation uuid from FS
        T41ResultsReader reader = new T41ResultsReader();
        T41ResultsPagesDTO resultToShowInTable = new T41ResultsPagesDTO();
        try {
            File outputFileResult =
                this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T41FileOutputFormat.OUTPUT_FILES_EXTENSION);
            if (!T41ResultsReader.isT41Results(outputFileResult)) {
                LOGGER.warn("t41PagesToShow() - File with extension: " + T41FileOutputFormat.OUTPUT_FILES_EXTENSION + " not Found!");
            }
            LOGGER.info("t41PagesToShow() - Reading output file: {}", outputFileResult.getName());
            resultToShowInTable = reader.getPagesToShow(outputFileResult);
        } catch (FileNotFoundException fnfe) {
            LOGGER.warn(fnfe.getMessage());
        }
        // Read logFile
        try {
            File outlogFile = this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T41FileOutputFormat.OUTPUT_LOG_FILE);
            if (!T41FileOutputFormat.OUTPUT_LOG_FILE.equals(outlogFile.getName())) {
                throw new FileNotFoundException("File : " + outlogFile.getName() + " not Found!");
            }
            LOGGER.info("t41PagesToShow() - Reading log file: {}", outlogFile.getName());
            List<T41LogInfoDTO> logInfoDTOList = reader.parseOutLogFile(outlogFile);
            if (logInfoDTOList != null && !logInfoDTOList.isEmpty()) {
                resultToShowInTable.setLogInfos(logInfoDTOList);
            }
        } catch (FileNotFoundException | ExcelReaderFileException outLogEx) {
            LOGGER.warn(
                "t41PagesToShow() - " +
                " Exception Unable to read log file: " +
                T41FileOutputFormat.OUTPUT_LOG_FILE +
                ", Exception: " +
                outLogEx.getMessage()
            );
        }

        try {
            LOGGER.info("t41PagesToShow() - Reading file: {}", ToolFileFormat.CONFIG_FILE);
            File launchFile = this.getLaunchFile(toolDto, uuid, ToolFileFormat.CONFIG_FILE);
            ObjectMapper objectMapper = new ObjectMapper();
            ToolConfigParameters configParameters = objectMapper.readValue(launchFile, ToolConfigParameters.class);
            LOGGER.debug("t41PagesToShow() - Reading configParameters: {}", configParameters.toString());
            resultToShowInTable.setToolConfigParameters(configParameters);
        } catch (FileNotFoundException fnfex) {
            LOGGER.warn(fnfex.getMessage());
        } catch (IOException ioe) {
            LOGGER.warn("t41PagesToShow() - Unable to read configuration's parameters from launch.json file: ", ioe.getMessage());
        }

        return resultToShowInTable;
    }

    @Override
    public T41TableDataDTO t41TablesData(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String title, Integer nSc) throws Exception {
        File outputFileResult = this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T41FileOutputFormat.OUTPUT_FILES_EXTENSION);
        if (!T41ResultsReader.isT41Results(outputFileResult)) {
            throw new FileNotFoundException("File : " + outputFileResult.getName() + " not Found!");
        }
        T41ResultsReader reader = new T41ResultsReader();
        return reader.parseResultsBySheetNameAndScenario(outputFileResult, title, nSc);
    }

    @Override
    public T41FlexResultsDTO t41Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid)
        throws FileNotFoundException, ExcelReaderFileException {
        LOGGER.info("t41Charts() - Reading file and prepare JSON file for charts visualization");

        File outputFileResult = this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T41FileOutputFormat.OUTPUT_FILES_EXTENSION);
        if (!T41ResultsReader.isT41Results(outputFileResult)) {
            throw new FileNotFoundException("File with extension: " + T41FileOutputFormat.OUTPUT_FILES_EXTENSION + " not Found!");
        }
        Map<String, List<FlexibleOption>> mapDataForSheet = new HashMap<String, List<FlexibleOption>>();
        T41ResultsReader reader = new T41ResultsReader();
        mapDataForSheet = reader.parseFlexDataSheets(outputFileResult);

        // outputDir doesn't contain file
        if (mapDataForSheet.isEmpty()) {
            // Tool doesn't produce output files
            T41FlexResultsDTO emptyResult = new T41FlexResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
            return emptyResult;
        }
        T41ResultsChartsDataSet resultsChartDataSet = new T41ResultsChartsDataSet();
        return resultsChartDataSet.t41PrepareChartsDataSet(mapDataForSheet);
    }

    // --- Start T44 tool's methods /ATTEST/tool/WP4/T44V3

    @Override
    public T44ResultsPagesDTO t44V3PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        T44ResultsPagesDTO resultToShowInTable = new T44ResultsPagesDTO();
        List<T44PageDTO> pages = new ArrayList<T44PageDTO>();

        Map<String, Integer> mapFile = this.t44V3TableResults(networkDto, toolDto, uuid);
        // mapFile = ["number_of_contingencies" : 50,  "normal" : 1, "number_of_scenarios" : 5]
        if (mapFile.containsKey(T44V3FileOutputFormat.TABLE_KEY_NORMAL)) {
            T44PageDTO normalPage = new T44PageDTO();
            normalPage.setTitle(T44V3FileOutputFormat.PAGE_NAME_NORMAL);
            pages.add(normalPage);
        }

        if (mapFile.containsKey(T44V3FileOutputFormat.TABLE_KEY_NUMBER_OF_CONTIN)) {
            T44PageDTO postContinPage = new T44PageDTO();
            postContinPage.setTitle(T44V3FileOutputFormat.PAGE_NAME_POST_CONTING);
            postContinPage.setNumContingencies(mapFile.get(T44V3FileOutputFormat.TABLE_KEY_NUMBER_OF_CONTIN));
            postContinPage.setNumScenarios(mapFile.get(T44V3FileOutputFormat.TABLE_KEY_NSC));

            List<Integer> contingencies = new ArrayList<>();
            for (int i = 1; i <= mapFile.get(T44V3FileOutputFormat.TABLE_KEY_NUMBER_OF_CONTIN); i++) {
                contingencies.add(i);
            }

            List<Integer> scenarios = new ArrayList<>();
            for (int i = 1; i <= mapFile.get(T44V3FileOutputFormat.TABLE_KEY_NSC); i++) {
                scenarios.add(i);
            }

            resultToShowInTable.setContingencies(contingencies);
            resultToShowInTable.setScenarios(scenarios);
            pages.add(postContinPage);
        }
        resultToShowInTable.setPages(pages);

        try {
            LOGGER.info("t44V3PagesToShow() - Reading launch.json file: {} ", ToolFileFormat.CONFIG_FILE);
            File launchFile = this.getLaunchFile(toolDto, uuid, ToolFileFormat.CONFIG_FILE);
            ObjectMapper objectMapper = new ObjectMapper();
            ToolConfigParameters configParameters = objectMapper.readValue(launchFile, ToolConfigParameters.class);
            LOGGER.debug("t44V3PagesToShow() - Reading configParameters: {} ", configParameters.toString());
            resultToShowInTable.setToolConfigParameters(configParameters);
        } catch (FileNotFoundException fnfex) {
            LOGGER.warn(fnfex.getMessage());
        } catch (IOException ioe) {
            LOGGER.warn("t44V3PagesToShow() - Unable to read configuration's parameters from launch.json file: ", ioe.getMessage());
        }
        return resultToShowInTable;
    }

    @Override
    public Map<String, Integer> t44V3TableResults(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        T44V3ResultsReader reader = new T44V3ResultsReader();
        Map<String, Integer> tableResults = new HashMap<>();

        try {
            // -- find file with extension '_post_contin.xlsx'
            File filePostContinResults =
                this.getOutputFileFilterByExtension(
                        networkDto,
                        toolDto,
                        uuid,
                        T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(1).concat(ExcelFileFormat.FILE_EXTENSION)
                    );
            // obtain number of contingencies and number of scenarios reading post_contin file Contin_map and Active_Power sheets
            tableResults = reader.parseContinMapAndActivePowerSheets(filePostContinResults);
            if (tableResults.isEmpty()) {
                tableResults = new HashMap<String, Integer>();
            }
        } catch (FileNotFoundException | ExcelReaderFileException ext) {
            LOGGER.info(
                "t44V3TableResults() - EXIT Results file with extension " +
                T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(1).concat(ExcelFileFormat.FILE_EXTENSION) +
                "  not found "
            );
        }

        try {
            // -- find file with extension '_Normal.xlsx'
            File fileNormalResults =
                this.getOutputFileFilterByExtension(
                        networkDto,
                        toolDto,
                        uuid,
                        T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(0).concat(ExcelFileFormat.FILE_EXTENSION)
                    );
            tableResults.put(T44V3FileOutputFormat.TABLE_KEY_NORMAL, 1);
        } catch (FileNotFoundException | ExcelReaderFileException ext) {
            LOGGER.info(
                "t44V3TableResults() - " +
                " Results file with extension " +
                T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(0).concat(ExcelFileFormat.FILE_EXTENSION) +
                "  not found "
            );
        }

        return ConverterUtils.mapReplaceKeyChar(tableResults, " ", "_", true);
    }

    @Override
    public T44ResultsDTO t44V3ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException {
        String outputDir = this.getOutputDir(networkDto, toolDto, uuid);
        LOGGER.info("t44V3ChartsByType() - Reading T44V3 output file and prepare JSON response file for charts visualization");
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

        T44V3ResultsChartsDataSet resultsChartDataSet = new T44V3ResultsChartsDataSet();
        return resultsChartDataSet.prepareChartsDataSet(mapAllExcelData);
    }

    @Override
    public T44ResultsDTO t44V3ChartsByNContingAndNsc(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String uuid,
        Integer idContin,
        Integer idScenario
    ) throws FileNotFoundException {
        File postContinResults =
            this.getOutputFileFilterByExtension(
                    networkDto,
                    toolDto,
                    uuid,
                    T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(1).concat(ExcelFileFormat.FILE_EXTENSION)
                );
        if (postContinResults == null) {
            // Tool doesn't produce output files
            T44ResultsDTO emptyResult = new T44ResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
            emptyResult.setFlexCosts(Collections.EMPTY_LIST);
            return emptyResult;
        }
        // read  sheets: "Active_power", "Reactive_power", "FL_inc", "FL_dec", "STR", "Load_curtail", "RES_curtail" and Costs
        T44V3ResultsReader readerV3 = new T44V3ResultsReader();
        Map<String, List<FlexibleOptionWithContin>> mapFlexibleOptions = readerV3.getPostContinDataByNumContinAndNumScenario(
            postContinResults,
            idContin,
            idScenario
        );

        LOGGER.debug(
            "t44V3ChartsByNContingAndNsc() - Data to Plot: " + Arrays.deepToString(mapFlexibleOptions.entrySet().stream().toArray())
        );
        T44V3ResultsChartsDataSet resultsChartDataSet = new T44V3ResultsChartsDataSet();
        return resultsChartDataSet.prepareChartsDataSet(mapFlexibleOptions);
    }

    // @type possible values are Normal or OPF
    @Override
    public T44ResultsDTO t44ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException {
        String outputDir = this.getOutputDir(networkDto, toolDto, uuid);

        LOGGER.debug("t44ChartsByType() - Reading  output files, prepare JSON response file for charts visualization");
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

    //-- T45
    @Override
    //20230508
    public ToolResultsPagesDTO t42T45PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception {
        LOGGER.debug("t42T45PagesToShow() - Network: {}, Tool:{}, uuid: {} ", networkDto, toolDto, uuid);
        File outputFileResult =
            this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T42T45FileOutputFormat.OUTPUT_FILES_EXTENSION);
        if (!T42T45ResultsReader.isT42T45Results(outputFileResult)) {
            throw new FileNotFoundException("File with extension: " + T42T45FileOutputFormat.OUTPUT_FILES_EXTENSION + " not Found!");
        }
        LOGGER.debug("t42T45PagesToShow() - Reading file: {} ", outputFileResult.getName());
        T42T45ResultsReader reader = new T42T45ResultsReader();
        ToolResultsPagesDTO resultToShowInTable = reader.getPagesToShow(outputFileResult);

        try {
            LOGGER.info("t42T45PagesToShow() - Reading file: {} ", ToolFileFormat.CONFIG_FILE);
            File launchFile = this.getLaunchFile(toolDto, uuid, ToolFileFormat.CONFIG_FILE);
            ObjectMapper objectMapper = new ObjectMapper();
            ToolConfigParameters configParameters = objectMapper.readValue(launchFile, ToolConfigParameters.class);
            LOGGER.debug("t42T45PagesToShow() - Reading configParameters: {} ", configParameters.toString());
            resultToShowInTable.setToolConfigParameters(configParameters);
        } catch (FileNotFoundException fnfex) {
            LOGGER.warn(fnfex.getMessage());
        } catch (IOException ioe) {
            LOGGER.warn("t42T45PagesToShow() - Unable to read configuration's parameters from launch.json file: ", ioe.getMessage());
        }
        return resultToShowInTable;
    }

    @Override
    public T42T45FlexResultsDTO t42T45Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String timePeriod)
        throws FileNotFoundException, ExcelReaderFileException {
        LOGGER.debug("t42T45Charts() - Network: {}, Tool:{}, uuid: {}, timePeriod: {}", networkDto, toolDto, uuid, timePeriod);
        File outputFileResult =
            this.getOutputFileFilterByExtension(networkDto, toolDto, uuid, T42T45FileOutputFormat.OUTPUT_FILES_EXTENSION);
        if (!T42T45ResultsReader.isT42T45Results(outputFileResult)) {
            throw new FileNotFoundException("File with extension: " + T42T45FileOutputFormat.OUTPUT_FILES_EXTENSION + " not Found!");
        }
        Map<String, List<FlexibleOption>> mapDataForSheet = new HashMap<String, List<FlexibleOption>>();
        T42T45ResultsReader reader = new T42T45ResultsReader();
        mapDataForSheet = reader.parseFlexDataSheets(outputFileResult);

        // outputDir doesn't contain file
        if (mapDataForSheet.isEmpty()) {
            // Tool doesn't produce output files
            T42T45FlexResultsDTO emptyResult = new T42T45FlexResultsDTO();
            emptyResult.setCharts(Collections.EMPTY_LIST);
        }

        T42T45ResultsChartsDataSet resultsChartDataSet = new T42T45ResultsChartsDataSet(timePeriod);
        T42T45FlexResultsDTO flexResultsDTO = resultsChartDataSet.prepareFlexChartsDataSet(mapDataForSheet);

        // Request Activation flexibility services for DSO
        List<T42T45ActivationDTO> requestForDSOActivations = reader.parseSheetRequestActivation(outputFileResult);
        flexResultsDTO.setActivations(requestForDSOActivations);
        LOGGER.debug("t42T45Charts() - END Reading output file, return: {}", flexResultsDTO);
        return flexResultsDTO;
    }

    @Override
    public String getOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid) {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/<TNUM>/<uuid>
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP4/<TNUM>/<uuid>/output_data
        String ouputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        LOGGER.debug("getOutputDir() - Return Dir: {}  ", ouputDir);
        return ouputDir;
    }

    @Override
    public File[] getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException {
        String outputDir = this.getOutputDir(networkDto, toolDto, uuid);

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found!");
        }
        File[] files = dir.listFiles();
        if (files.length == 0) {
            throw new FileNotFoundException("Directory: " + outputDir + " is empty!");
        }
        return files;
    }

    @Override
    public byte[] zipFilesFromOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException {
        File[] filesToZip = getOutputFile(networkDto, toolDto, uuid);
        return FileUtils.zipFiles(filesToZip).toByteArray();
    }

    @Override
    public byte[] zipOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException {
        String outputDir = this.getOutputDir(networkDto, toolDto, uuid);
        File dirToZip = new File(outputDir);
        if (!dirToZip.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found!");
        }
        return FileUtils.zipDir(dirToZip).toByteArray();
    }

    public File getOutputFileFilterByExtension(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String extension)
        throws FileNotFoundException {
        String outputDir = this.getOutputDir(networkDto, toolDto, uuid);

        LOGGER.info("getOutputFileFilterByExtension() - Search output file in dir: {}, with extension: {}", outputDir, extension);
        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " not found");
        }

        FilenameFilter filterByExtension = new FilenameFilter() {
            @Override
            public boolean accept(File directory, String fileName) {
                // LOGGER.debug("FileName: {} - Filter for extension {}, fileNameEndWithExtension: {}", fileName, extension, fileName.endsWith(extension));
                return ((fileName.endsWith(extension) || fileName.equals(extension)) && !fileName.startsWith("~$"));
            }
        };

        File[] files = dir.listFiles(filterByExtension);
        //tool doesn't produce any file
        if (files.length == 0) {
            throw new FileNotFoundException("Directory: " + outputDir + ", doesn't contain file with extension: " + extension);
        }

        if (files.length > 1) {
            throw new FileNotFoundException("Directory: " + outputDir + ", contains more files than expected!");
        }

        File excelFile = files[0];

        LOGGER.info("getOutputFileFilterByExtension() - EXIT  Return  file: {} ", excelFile.getName());
        return excelFile;
    }

    /**
     * @param toolDto DTO of the entity Tool
     * @param uuid simulation unique identifier
     * @param launchFileName file used for running the tool (usually 'launch.json')
     * @return file used for storing all configuration's parameters used by the tool
     * @throws FileNotFoundException if file or directory containing the file is not present on file system
     */
    public File getLaunchFile(ToolDTO toolDto, String uuid, String launchFileName) throws FileNotFoundException {
        LOGGER.info("getLaunchFile() -  Tool: {}, uuid: {}, launchFileName: {} ", toolDto.getNum(), uuid, launchFileName);
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP4/<TNUM>/<uuid>
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        LOGGER.info("getLaunchFile() -  simulationWorkingDir: {}", simulationWorkingDir);

        File dir = new File(simulationWorkingDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + simulationWorkingDir + " not found!");
        }

        FilenameFilter filterByName = new FilenameFilter() {
            @Override
            public boolean accept(File directory, String fileName) {
                LOGGER.debug("getLaunchFile() -  Filter for fileName {} is equals: {}", fileName, fileName.equals(launchFileName));
                return (fileName.equals(launchFileName));
            }
        };

        File[] files = dir.listFiles(filterByName);
        //tool doesn't produce any file
        if (files != null && files.length == 0) {
            throw new FileNotFoundException("Directory: " + simulationWorkingDir + ", doesn't contain file: " + launchFileName);
        }
        if (files != null && files.length > 1) {
            throw new FileNotFoundException("Directory: " + simulationWorkingDir + ", contains more files than expected!");
        }

        File launchFile = files[0];
        LOGGER.info("getLaunchFile() - EXIT Return file: {} ", launchFile.getName());
        return launchFile;
    }
}
