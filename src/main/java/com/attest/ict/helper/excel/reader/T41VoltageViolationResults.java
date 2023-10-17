package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T41VoltageViolationDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41VoltageViolationResults {

    public final Logger log = LoggerFactory.getLogger(T41VoltageViolationResults.class);

    private T41VoltageViolationDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);

        try {
            return new T41VoltageViolationDTO(
                val[0], // Itr
                val[1], // Num
                Double.valueOf(val[2]).intValue(), // Scen
                Double.valueOf(val[3]).intValue(), // T
                Double.valueOf(val[4]).intValue(), // Bus
                ConverterUtils.roundStringValue(val[5]), // V
                ConverterUtils.roundStringValue(val[6]) // Viol(V)
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T41VoltageViolationDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T41VoltageViolationDTO> setRowData(Map<Integer, List<String>> mapData, String sheetName, Integer nSc) {
        try {
            List<T41VoltageViolationDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T41VoltageViolationDTO voltageViolationDto = mapDataToDTO(data);
                if (voltageViolationDto != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return (nSc == null) ? rows : rows.stream().filter(r -> r.getScen().equals(nSc)).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {} Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
