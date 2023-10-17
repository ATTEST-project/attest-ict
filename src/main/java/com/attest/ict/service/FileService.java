package com.attest.ict.service;

import com.attest.ict.domain.Network;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ByteArrayInputStream load(Date startDate, Date endDate);
}
