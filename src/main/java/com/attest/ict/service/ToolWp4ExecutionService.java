package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.tools.exception.RunningToolException;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp4ExecutionService {
    String windAndPV(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception;

    String tractability(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws RunningToolException, Exception;

    String t44AsDayheadTx(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws RunningToolException, Exception;
}
