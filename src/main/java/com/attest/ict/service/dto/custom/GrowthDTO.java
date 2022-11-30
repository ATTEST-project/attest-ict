package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;

public class GrowthDTO implements Serializable {

    @JsonProperty("Active")
    Map<String, Number> active;

    @JsonProperty("Slow")
    Map<String, Number> slow;

    public Map<String, Number> getActive() {
        return active;
    }

    public void setActive(Map<String, Number> active) {
        this.active = active;
    }

    public Map<String, Number> getSlow() {
        return slow;
    }

    public void setSlow(Map<String, Number> slow) {
        this.slow = slow;
    }

    @Override
    public String toString() {
        return "Growth [active=" + active + ", slow=" + slow + "]";
    }
}
