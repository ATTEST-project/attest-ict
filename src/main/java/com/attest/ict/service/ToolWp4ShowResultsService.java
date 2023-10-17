package com.attest.ict.service;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface ToolWp4ShowResultsService {
    //--- TSG
    TSGResultsDTO windAndPVCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException, OdsReaderFileException;
    //--- T41
    T41ResultsPagesDTO t41PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception;
    T41TableDataDTO t41TablesData(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String sheetName, Integer nSc) throws Exception;
    T41FlexResultsDTO t41Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException, ExcelReaderFileException;

    //--- T44V3 and Demo
    T44ResultsPagesDTO t44V3PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;
    Map<String, Integer> t44V3TableResults(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) throws FileNotFoundException;
    T44ResultsDTO t44V3ChartsByNContingAndNsc(NetworkDTO networkDto, ToolDTO toolDto, String uuid, Integer idContin, Integer idScenario)
        throws FileNotFoundException;
    T44ResultsDTO t44ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException;
    T44ResultsDTO t44V3ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException;
    byte[] zipFilesFromOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException;

    //-- T42, T45 and T45_Final
    ToolResultsPagesDTO t42T45PagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception;
    T42T45FlexResultsDTO t42T45Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String timePeriod)
        throws FileNotFoundException, ExcelReaderFileException;

    String getOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid);
    File[] getOutputFile(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) throws FileNotFoundException;
    byte[] zipOutputDir(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException;
}
