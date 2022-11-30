package com.attest.ict.helper.matpower.flexibility.annotated;

import com.attest.ict.domain.FlexProfile;
import com.attest.ict.helper.matpower.flexibility.annotated.converter.TimeIntervalConverter;
import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;

public class FlexProfileAnnotated extends FlexProfile {

    /*@Parsed(index = 0)
    public int getMode() {
        return super.getMode();
    }*/

    @Parsed(index = 0, field = "mode")
    public void setMode(int mode) {
        super.setMode(mode);
    }

    /*@Parsed(index = 1)
    @Convert(conversionClass = TimeIntervalConverter.class)
    public int getTimeInterval() {
        return super.getTimeInterval();
    }*/

    @Parsed(index = 1, field = "timeInterval")
    // @Convert(conversionClass = TimeIntervalConverter.class)
    public void setTimeInterval(Double timeInterval) {
        /*int convertedValue = 60;
        if (timeInterval == 0.5) {
            convertedValue = 30;
        }
        else if (timeInterval == 0.25) {
            convertedValue = 15;
        }*/
        super.setTimeInterval(timeInterval);
    }
}
