package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.BusCoordinate;
import com.univocity.parsers.annotations.Parsed;

public class BusCoordinatesAnnotated extends BusCoordinate {

    @Parsed(index = 0)
    public Double getX() {
        return super.getX();
    }

    @Parsed(index = 0, field = "x")
    public void setX(Double x) {
        super.setX(x);
    }

    @Parsed(index = 1)
    public Double getY() {
        return super.getY();
    }

    @Parsed(index = 1, field = "y")
    public void setY(Double y) {
        super.setY(y);
    }
}
