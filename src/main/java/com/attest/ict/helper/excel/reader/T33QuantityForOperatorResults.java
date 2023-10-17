package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33QuantityForNodeAndOperatorDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33QuantityForOperatorResults {

    public final Logger log = LoggerFactory.getLogger(T33QuantityForOperatorResults.class);
    final String NOT_AVAILABLE = "N/A";

    private T33QuantityForNodeAndOperatorDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        int startIndex = (val.length > 30) ? 1 : 0;
        data.toArray(val);
        try {
            return new T33QuantityForNodeAndOperatorDTO(
                Double.valueOf(val[0]).intValue(), // nodeID
                this.getOperator(val), // Operator
                Double.valueOf(val[startIndex + 1]).intValue(), // Year
                val[startIndex + 2], // Day
                val[startIndex + 3], // Quantity
                val[startIndex + 4], // marketScenario
                ConverterUtils.removeDotZero(val[startIndex + 5]), // operationScenario
                val[startIndex + 6].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 6]), //  t0  value at hour:00:00
                val[startIndex + 7].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 7]), //  t0  value at hour:00:00
                val[startIndex + 8].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 8]), //  t1
                val[startIndex + 9].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 9]),
                val[startIndex + 10].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 10]),
                val[startIndex + 11].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 11]),
                val[startIndex + 12].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 12]),
                val[startIndex + 13].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 13]),
                val[startIndex + 14].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 14]),
                val[startIndex + 15].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 15]),
                val[startIndex + 16].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 16]),
                val[startIndex + 17].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 17]),
                val[startIndex + 18].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 18]),
                val[startIndex + 19].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 19]),
                val[startIndex + 20].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 20]),
                val[startIndex + 21].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 21]),
                val[startIndex + 22].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 22]),
                val[startIndex + 23].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 23]),
                val[startIndex + 24].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 24]),
                val[startIndex + 25].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 25]),
                val[startIndex + 26].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 26]),
                val[startIndex + 27].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 27]),
                val[startIndex + 28].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 28]),
                val[startIndex + 29].equals(NOT_AVAILABLE) ? NOT_AVAILABLE : ConverterUtils.roundValue(val[startIndex + 29])
            );
        } catch (Exception ex) {
            log.error("Convert excel Row:{} into T33SharedEssDTO. Exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33QuantityForNodeAndOperatorDTO> setRowData(
        Map<Integer, List<String>> mapData,
        String sheetName,
        List<String> filtersMarket
    ) {
        try {
            List<T33QuantityForNodeAndOperatorDTO> rows = new ArrayList<T33QuantityForNodeAndOperatorDTO>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33QuantityForNodeAndOperatorDTO sharedEssDTO = mapDataToDTO(data);
                if (sharedEssDTO != null) {
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

    /**
     * @param val
     * @return 'ESSO' if sheet 'Shared ESS' doesn't contain column Operator, otherwise the value present in the second column
     */
    private String getOperator(String[] val) {
        log.debug(" getOperator headerLenght" + val.length);
        String defaultOperator = "ESSO";
        return (val.length > 30) ? val[1] : defaultOperator;
    }
}
