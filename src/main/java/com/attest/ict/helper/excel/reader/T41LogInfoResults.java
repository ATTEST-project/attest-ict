package com.attest.ict.helper.excel.reader;

import com.attest.ict.service.dto.custom.T41LogInfoDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41LogInfoResults {

    public final Logger log = LoggerFactory.getLogger(T41LogInfoResults.class);

    private T41LogInfoDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);

        try {
            return new T41LogInfoDTO(
                Double.valueOf(val[0]).intValue(),
                val[1], // case Name
                val[2], // InputFile
                val[3], // Output File
                LocalDateTime.parse(val[4]), // startTime
                val[5], // intPf
                val[6], // nViolation
                val[7], // optStarted
                val[8] // optFound
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T41LogInfoDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T41LogInfoDTO> setRowData(Map<Integer, List<String>> mapData, String sheetName) {
        try {
            List<T41LogInfoDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T41LogInfoDTO logInfoDTO = mapDataToDTO(data);
                if (logInfoDTO != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return rows;
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {} Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
