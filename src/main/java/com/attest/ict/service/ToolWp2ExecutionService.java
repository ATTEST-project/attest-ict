package com.attest.ict.service;

import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

public interface ToolWp2ExecutionService {
    Map<String, String> prepareT25WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception;

    Map<String, String> prepareT26WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception;

    CompletableFuture<String> t26Async(NetworkDTO networkDto, ToolDTO toolDto, Map<String, String> toolConfigMap) throws Exception;
    //CompletableFuture<String> t25Async(NetworkDTO networkDto, ToolDTO toolDto, Map<String, String> toolConfigMap) throws Exception;

    //String t26(NetworkDTO networkDTO, ToolDTO toolDTO, MultipartFile[] files, String jsonConfig) throws Exception;
    //String t25(NetworkDTO networkDTO, ToolDTO toolDTO, MultipartFile[] files, String jsonConfig) throws Exception;

}
