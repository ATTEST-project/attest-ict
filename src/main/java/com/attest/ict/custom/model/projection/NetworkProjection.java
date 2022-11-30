package com.attest.ict.custom.model.projection;

import java.util.Date;
import java.util.List;

public interface NetworkProjection {
    String getNetworkId();
    String getName();
    String getMpcName();
    String getCountry();
    String getType();
    Date getNetworkDate();
    int getVersion();
    Date getCreationDatetime();
    Date getUpdateDatetime();
    double getBaseMVA();
    List<Long> getBuses();

    // unused since getBuses() already returns a list of Long values
    // here just to remember it
    interface BusProjection {
        Long getBusId();
    }
}
