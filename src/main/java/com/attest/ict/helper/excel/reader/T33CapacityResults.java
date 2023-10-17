package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33CapacityDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33CapacityResults {

    public final Logger log = LoggerFactory.getLogger(T33CapacityResults.class);

    private T33CapacityDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            // Integer node, String quantity, Double year2020, Double year2030, Double year2040, Double year2050
            return new T33CapacityDTO(
                Double.valueOf(val[0]).intValue(), // node
                this.addPercentSymbol(val[1]), // quantity
                this.roundValue(2, val), // 2020
                this.roundValue(3, val), // 2030
                this.roundValue(4, val), // 2040
                this.roundValue(5, val) // 2050
            );
        } catch (Exception ex) {
            log.error(" Error Converting excel Row:{} into T33CapacityDTO, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    private Double roundValue(int index, String[] val) {
        int pos = val[index].indexOf("%");
        String value = (pos > 0) ? val[index].substring(0, pos - 1) : val[index];
        return ConverterUtils.roundStringValue(value);
    }

    private String addPercentSymbol(String quantity) {
        return quantity.equals("Degradation factor") ? quantity + ", [%]" : quantity;
    }

    public List<T33CapacityDTO> setCapacityRowData(Map<Integer, List<String>> mapData, List<String> filtersQuantity, String sheetName) {
        try {
            List<T33CapacityDTO> rows = new ArrayList<>();
            for (var entry : mapData.entrySet()) {
                List<String> data = entry.getValue();
                T33CapacityDTO capacityDTO = mapDataToDTO(data);
                if (capacityDTO != null) {
                    rows.add(mapDataToDTO(data));
                }
            }
            return (filtersQuantity.size() > 0)
                ? rows.stream().filter(r -> filtersQuantity.contains(r.getQuantity())).collect(Collectors.toList())
                : rows;
        } catch (Exception ex) {
            log.error("Exception preparing data to visualize sheet: {} Error: {} ", sheetName, ex.getMessage());
            return Collections.emptyList();
        }
    }
}
