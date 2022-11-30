package com.attest.ict.service;

import com.attest.ict.domain.Network;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public interface ExcelGenProfileService {
    public void genProfile(MultipartFile file, Optional<Network> networkOpt, Boolean headerEnabled);

    public void genProfile(
        MultipartFile file,
        Optional<Network> networkOpt,
        Integer mode,
        String season,
        String typicalDay,
        Boolean headerEnabled
    );
}
