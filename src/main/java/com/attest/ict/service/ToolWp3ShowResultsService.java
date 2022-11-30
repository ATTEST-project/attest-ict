package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.io.File;
import java.io.FileNotFoundException;

public interface ToolWp3ShowResultsService {
    File getOutputFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;
}
