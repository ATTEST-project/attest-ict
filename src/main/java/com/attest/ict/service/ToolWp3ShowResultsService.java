package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T33ResultsPagesDTO;
import com.attest.ict.service.dto.custom.TableDataDTO;
import java.io.File;
import java.io.FileNotFoundException;

public interface ToolWp3ShowResultsService {
    File getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;

    T33ResultsPagesDTO getPagesToShow(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws Exception;

    TableDataDTO getTablesData(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String node, String day, String title) throws Exception;
}
