package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T41LineViolationDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41LineViolationResults {

    public final Logger log = LoggerFactory.getLogger(T41LineViolationResults.class);

    private T41LineViolationDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);

        try {
            return new T41LineViolationDTO(
                val[0], // Itr
                val[1], // Num
                Double.valueOf(val[2]).intValue(), // Scen
                Double.valueOf(val[3]).intValue(), // T
                Double.valueOf(val[4]).intValue(), // fBus
                Double.valueOf(val[4]).intValue(), // tBus
                ConverterUtils.roundStringValue(val[5]), // s
                ConverterUtils.roundStringValue(val[6]), // sLimit
                ConverterUtils.roundStringValue(val[7]) // Violation
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T41LineViolationDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T41LineViolationDTO> setRowData(Map<Integer, List<String>> mapData, String sheetName, Integer scenario) {
        try {
            List<T41LineViolationDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T41LineViolationDTO lineViolationDTO = mapDataToDTO(data);
                if (lineViolationDTO != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return (scenario == null) ? rows : rows.stream().filter(r -> r.getScen().equals(scenario)).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {} Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
