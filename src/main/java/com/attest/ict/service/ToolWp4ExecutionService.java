package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp4ExecutionService {
    @Transactional
    Map<String, String> prepareTSGWorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception;

    @Transactional
    Map<String, String> prepareT41WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception;

    @Transactional
    Map<String, String> prepareT44V3WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception;

    @Transactional
    Map<String, String> prepareT42WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues,
        Long[] otherToolOutputFileIds
    ) throws Exception;

    @Transactional
    Map<String, String> prepareT45WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues,
        Long[] otherToolOutputFileIds
    ) throws Exception;
}
