package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33QuantityFromNodeToNodeDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33QuantityFromNodeToNode {

    public final Logger log = LoggerFactory.getLogger(T33QuantityFromNodeToNode.class);

    private T33QuantityFromNodeToNodeDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            return new T33QuantityFromNodeToNodeDTO(
                val[0], // Operator
                ConverterUtils.removeDotZero(val[1]), // Connection Node ID
                Double.valueOf(val[2]).intValue(), // From Node
                Double.valueOf(val[3]).intValue(), // To Node
                Double.valueOf(val[4]).intValue(), // Year
                val[5], // Day
                val[6], // Quantity
                val[7], // marketScenario
                ConverterUtils.removeDotZero(val[8]), //  operationScenario
                ConverterUtils.roundValue(val[9]), //to
                ConverterUtils.roundValue(val[10]), //t1
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
                ConverterUtils.roundValue(val[31]),
                ConverterUtils.roundValue(val[32])
            );
        } catch (Exception ex) {
            log.error("Convert excel Row:{} into T33QuantityFromNodeToNodeDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33QuantityFromNodeToNodeDTO> setQuantityFromNodeToNode(
        Map<Integer, List<String>> mapData,
        String sheetName,
        List<String> filtersMarket
    ) {
        try {
            List<T33QuantityFromNodeToNodeDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33QuantityFromNodeToNodeDTO quantityFronNodeToNode = mapDataToDTO(data);
                if (quantityFronNodeToNode != null) {
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
