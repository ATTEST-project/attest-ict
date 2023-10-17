package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T41QuantityForScenAndIdDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T41QuantityForScenAndIdResults {

    public final Logger log = LoggerFactory.getLogger(T41QuantityForScenAndIdResults.class);

    private T41QuantityForScenAndIdDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);

        try {
            return new T41QuantityForScenAndIdDTO(
                Double.valueOf(val[0]).intValue(), // Scenario
                Double.valueOf(val[1]).intValue(), // Id
                ConverterUtils.roundValue(val[2]), // value at  instant eg. hour 00
                ConverterUtils.roundValue(val[3]), // value at  instant eg. hour 01
                ConverterUtils.roundValue(val[4]),
                ConverterUtils.roundValue(val[5]),
                ConverterUtils.roundValue(val[6]),
                ConverterUtils.roundValue(val[7]),
                ConverterUtils.roundValue(val[8]),
                ConverterUtils.roundValue(val[9]),
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
                ConverterUtils.roundValue(val[25])
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T41QuantityForScenAndIdDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T41QuantityForScenAndIdDTO> setRowData(Map<Integer, List<String>> mapData, String sheetName, Integer scenario) {
        try {
            List<T41QuantityForScenAndIdDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T41QuantityForScenAndIdDTO quantityForScenAndIdDTO = mapDataToDTO(data);
                if (quantityForScenAndIdDTO != null) {
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
