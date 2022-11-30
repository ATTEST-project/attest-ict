package com.attest.ict.service;

import com.attest.ict.domain.Network;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing Attest Files
 */
public interface FileService {
    ByteArrayInputStream load(Date startDate, Date endDate);

    InputStream getNetworkData(String networkName) throws IOException;

    //void save(MultipartFile file, String networkName, Long networkId) throws IOException, ParseException;

    void save(MultipartFile file, Network network) throws IOException, ParseException;
}
