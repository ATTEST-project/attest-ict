package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33OfValueDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33OfValueResults {

    public final Logger log = LoggerFactory.getLogger(T33OfValueResults.class);

    private T33OfValueDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            // String agent,  Double year2020, Double year2030, Double year2040, Double year2050
            return new T33OfValueDTO(
                val[0], // agent,
                ConverterUtils.roundStringValue(val[1]), // 2020
                ConverterUtils.roundStringValue(val[2]), // 2030
                ConverterUtils.roundStringValue(val[3]), // 2040
                ConverterUtils.roundStringValue(val[4]), // 2050
                ConverterUtils.roundStringValue(val[5]) // Total
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T33OfValueResults, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33OfValueDTO> setOfValueRowData(Map<Integer, List<String>> mapData, String sheetName) {
        try {
            List<T33OfValueDTO> rows = new ArrayList<T33OfValueDTO>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33OfValueDTO ofValueDTO = mapDataToDTO(data);
                if (ofValueDTO != null) {
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
