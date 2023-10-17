package com.attest.ict.service;

import com.attest.ict.helper.matpower.exception.MatpowerReaderFileException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MatpowerNetworkService {
    void importFromMatpowerFile(MultipartFile mpFile, Long networkId) throws IOException, MatpowerReaderFileException;

    InputStream exportToMatpowerFile(String networkName) throws IOException, MatpowerReaderFileException;
}
