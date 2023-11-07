package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.tools.exception.RunningToolException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp5ExecutionService {
    String[] getFileHeaders(MultipartFile mpfile);

    Map<String, String> prepareT511CharacterizationWorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String jsonConfig,
        MultipartFile[] files
    ) throws RunningToolException, Exception;

    Map<String, String> prepareT512MonitoringWorkingDir(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws Exception;

    Map<String, String> prepareT52WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception;

    Map<String, String> prepareT53WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception;
}
