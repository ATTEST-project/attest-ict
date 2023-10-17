package com.attest.ict.helper.excel.reader;

import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.service.dto.custom.T33CapacityDTO;
import com.attest.ict.service.dto.custom.T33ConvergenceDTO;
import com.attest.ict.service.dto.custom.T33EssSecondaryReserveDTO;
import com.attest.ict.service.dto.custom.T33MainInfoDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T33SecondaryReserveResults {

    public final Logger log = LoggerFactory.getLogger(T33SecondaryReserveResults.class);

    private T33EssSecondaryReserveDTO mapDataToDTO(List<String> data) {
        String[] val = new String[data.size()];
        data.toArray(val);
        try {
            return new T33EssSecondaryReserveDTO(
                Double.valueOf(val[0]).intValue(), // year
                val[1], // Day
                val[2], // Type
                ConverterUtils.roundValue(val[3]), // value at to instant eg. hour 00 ( Warning value cound be N/A)
                ConverterUtils.roundValue(val[4]), // 0re 1
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
                ConverterUtils.roundValue(val[25]),
                ConverterUtils.roundValue(val[26])
            );
        } catch (Exception ex) {
            log.error("Convert excel Row:{} into T33SecondaryReserveResults, exception: {}", data, ex.getMessage());
            return null;
        }
    }

    public List<T33EssSecondaryReserveDTO> setEssSecondaryReserveRowData(Map<Integer, List<String>> mapData, String sheetName) {
        try {
            List<T33EssSecondaryReserveDTO> rows = new ArrayList<T33EssSecondaryReserveDTO>();
            for (var entry : mapData.entrySet()) {
                Integer rowIndex = entry.getKey();
                List<String> data = entry.getValue();
                T33EssSecondaryReserveDTO secondaryReserveDTO = mapDataToDTO(data);
                if (secondaryReserveDTO != null) {
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
