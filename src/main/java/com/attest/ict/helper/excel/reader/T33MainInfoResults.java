package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33MainInfoDTO;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33MainInfoResults {

    public final Logger log = LoggerFactory.getLogger(T33MainInfoResults.class);

    private T33MainInfoDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            // String agent, NodeId, Value,  Double year2020, Double year2030, Double year2040, Double year2050
            return new T33MainInfoDTO(
                val[0], // agent,
                val[1], // Node ID,
                val[2], // Value,
                ConverterUtils.roundStringValue(val[3]), // 2020 Summer
                ConverterUtils.roundStringValue(val[4]), // 2020 Winter
                ConverterUtils.roundStringValue(val[5]), // 2030 Summer
                ConverterUtils.roundStringValue(val[6]), // 2030 Winter
                ConverterUtils.roundStringValue(val[7]), // 2040 Summer
                ConverterUtils.roundStringValue(val[8]), // 2040 Winter
                ConverterUtils.roundStringValue(val[9]), // 2050 Summer
                ConverterUtils.roundStringValue(val[10]) // 2050 Winter
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T33MainInfoResults, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33MainInfoDTO> setMainInfoData(Map<Integer, List<String>> mapData, String sheetName) {
        try {
            List<T33MainInfoDTO> rows = new ArrayList<T33MainInfoDTO>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T33MainInfoDTO mainInfoDTO = mapDataToDTO(data);
                if (mainInfoDTO != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return rows;
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {}. Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
