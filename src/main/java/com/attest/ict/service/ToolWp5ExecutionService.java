package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.tools.exception.RunningToolException;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp5ExecutionService {
    String[] getFileHeaders(MultipartFile mpfile);

    String t52Run(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files) throws RunningToolException, Exception;

    String t512MonitoringRun(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception;

    String t511CharacterizationRun(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception;

    String t53Run(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files) throws RunningToolException, Exception;
}
