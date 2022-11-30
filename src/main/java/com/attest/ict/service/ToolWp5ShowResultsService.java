package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ToolWp5ShowResultsService {
    List<String> getResultsFileName(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;

    File[] getResultsFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid, List<String> fileExtensions) throws FileNotFoundException;

    File[] downloadResultsFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws FileNotFoundException;

    String readFile(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String fileName) throws IOException;
}
