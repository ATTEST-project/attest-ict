package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T31InputParamDTO;
import com.attest.ict.tools.exception.RunningToolException;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp3ExecutionService {
    String t31Run(NetworkDTO networkDto, ToolDTO toolDto, T31InputParamDTO params) throws RunningToolException, Exception;

    String t32Run(NetworkDTO networkDTO, ToolDTO toolDTO, Long profileId, MultipartFile[] files, String jsonConfig)
        throws RunningToolException, Exception;
}
