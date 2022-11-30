package com.attest.ict.service;

import com.attest.ict.helper.excel.exception.ExcelReaderFileException;
import com.attest.ict.helper.ods.exception.OdsReaderFileException;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T41ResultsDTO;
import com.attest.ict.service.dto.custom.T44ResultsDTO;
import com.attest.ict.service.dto.custom.TSGResultsDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public interface ToolWp4ShowResultsService {
    Map<String, Integer> t44TableResults(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;

    T44ResultsDTO t44ChartsByNContingAndNsc(NetworkDTO networkDto, ToolDTO toolDto, String uuid, Integer nConting, Integer nSc)
        throws FileNotFoundException;

    T44ResultsDTO t44ChartsByType(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String type) throws FileNotFoundException;

    T41ResultsDTO t41Charts(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException, ExcelReaderFileException;

    File[] getOutputFile(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) throws FileNotFoundException;

    TSGResultsDTO windAndPVCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException, OdsReaderFileException;
}
