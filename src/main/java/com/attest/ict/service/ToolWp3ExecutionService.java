package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp3ExecutionService {
    @Transactional
    Map<String, String> prepareT31WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception;

    @Transactional
    Map<String, String> prepareT32WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        Long profileId,
        MultipartFile[] mpFiles,
        String jsonConfig
    ) throws Exception;

    @Transactional
    Map<String, String> prepareT33WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile mpFile, String jsonConfig)
        throws Exception;
}
