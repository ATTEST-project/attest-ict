package com.attest.ict.service;

import com.attest.ict.helper.ods.exception.OdsWriterFileException;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public interface OdsNetworkService {
    // InputStream exportNetworkToOdsFile(Long networkId, String fileName) throws IOException;

    //22/08/2022  add new method to import network data used by T41_v2 seems not to be possible using test cases data as is
    void importNetworkFromOdsFile(Long networkId, MultipartFile file);

    /**
     * @param networkId
     * @param filePath
     * @return ODS file with network data one componenr for each sheet:
     *  (buses, branches, loads, generators, baseMVA)
     */
    ByteArrayOutputStream exportNetworkToOdsFile(Long networkId) throws OdsWriterFileException;
}
