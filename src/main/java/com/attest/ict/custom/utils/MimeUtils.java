package com.attest.ict.custom.utils;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public final class MimeUtils {
    public static String detect (MultipartFile multipartFile) {
        Tika tika = new Tika();
        try {
            return tika.detect(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
