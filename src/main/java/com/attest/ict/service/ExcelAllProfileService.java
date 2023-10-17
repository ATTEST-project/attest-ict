package com.attest.ict.service;

import com.attest.ict.domain.Network;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public interface ExcelAllProfileService {
    public void allProfile(MultipartFile file, Network network, Integer mode, String season, String typicalDay) throws IOException;
}
