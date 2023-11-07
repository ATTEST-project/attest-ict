package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33CapacityDTO;
import com.attest.ict.service.dto.custom.T33ConvergenceDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33Convergence {

    public final Logger log = LoggerFactory.getLogger(T33Convergence.class);

    private T33ConvergenceDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            if (data.size() > 2) {
                return new T33ConvergenceDTO(
                    Double.valueOf(val[0]).intValue(), // iteration
                    ConverterUtils.roundStringValue(val[1]), // Lower Bound
                    ConverterUtils.roundStringValue(val[2])
                );
            } else {
                log.warn("Missing Upper Bound for Row {} ", data);
                return null;
            }
        } catch (Exception ex) {
            log.error("Convert excel Row:{} into T33ConvergenceDTO. Exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33ConvergenceDTO> setConvergenceRowData(Map<Integer, List<String>> mapData, String sheetName) {
        try {
            List<T33ConvergenceDTO> rows = new ArrayList<T33ConvergenceDTO>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33ConvergenceDTO convergenceDTO = mapDataToDTO(data);
                if (convergenceDTO != null) {
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
