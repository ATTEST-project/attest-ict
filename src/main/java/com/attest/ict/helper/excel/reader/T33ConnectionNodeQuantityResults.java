package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33ConnectionNodeQuantityDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33ConnectionNodeQuantityResults {

    public final Logger log = LoggerFactory.getLogger(T33ConnectionNodeQuantityResults.class);

    private T33ConnectionNodeQuantityDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            return new T33ConnectionNodeQuantityDTO(
                val[0], // Operator
                ConverterUtils.removeDotZero(val[1]), // Connection Node ID
                Double.valueOf(val[2]).intValue(), // Network Node Id
                Double.valueOf(val[3]).intValue(), // Year
                val[4], // Day
                val[5], // Quantity
                val[6], // marketScenario
                ConverterUtils.removeDotZero(ConverterUtils.removeDotZero(val[7])), //  operationScenario
                ConverterUtils.roundValue(val[8]), //  t0
                ConverterUtils.roundValue(val[9]), // t1
                ConverterUtils.roundValue(val[10]),
                ConverterUtils.roundValue(val[11]),
                ConverterUtils.roundValue(val[12]),
                ConverterUtils.roundValue(val[13]),
                ConverterUtils.roundValue(val[14]),
                ConverterUtils.roundValue(val[15]),
                ConverterUtils.roundValue(val[16]),
                ConverterUtils.roundValue(val[17]),
                ConverterUtils.roundValue(val[18]),
                ConverterUtils.roundValue(val[19]),
                ConverterUtils.roundValue(val[20]),
                ConverterUtils.roundValue(val[21]),
                ConverterUtils.roundValue(val[22]),
                ConverterUtils.roundValue(val[23]),
                ConverterUtils.roundValue(val[24]),
                ConverterUtils.roundValue(val[25]),
                ConverterUtils.roundValue(val[26]),
                ConverterUtils.roundValue(val[27]),
                ConverterUtils.roundValue(val[28]),
                ConverterUtils.roundValue(val[29]),
                ConverterUtils.roundValue(val[30]),
                ConverterUtils.roundValue(val[31])
            );
        } catch (Exception ex) {
            log.error("Convert excel Row:{} into T33ConnectionNodeQuantityDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33ConnectionNodeQuantityDTO> setRowData(Map<Integer, List<String>> mapData, String sheetName, List<String> filtersMarket) {
        try {
            List<T33ConnectionNodeQuantityDTO> rows = new ArrayList<T33ConnectionNodeQuantityDTO>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33ConnectionNodeQuantityDTO voltageConsumtionDTO = mapDataToDTO(data);
                if (voltageConsumtionDTO != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return (filtersMarket.size() > 0)
                ? rows.stream().filter(r -> filtersMarket.contains(r.getMarketScenario())).collect(Collectors.toList())
                : rows;
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {}. Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
