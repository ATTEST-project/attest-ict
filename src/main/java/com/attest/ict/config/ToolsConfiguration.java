package com.attest.ict.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tools")
public class ToolsConfiguration {

    //From application*.yaml
    //tools:
    //	  path: /ATTEST/Tools/

    @Value("${tools.path}")
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // tools:
    //		pathSimulation: /ATSIM
    @Value("${tools.pathSimulation}")
    String pathSimulation;

    public String getPathSimulation() {
        return pathSimulation;
    }

    public void setSimulation(String pathSimulation) {
        this.pathSimulation = pathSimulation;
    }

    // tools:
    //		pathSimulation: /ATSIM
    @Value("${tools.t44WithConv}")
    String t44WithConv;

    public String getT44WithConv() {
        return t44WithConv;
    }

    public void setT44WithConv(String t44WithConv) {
        this.t44WithConv = t44WithConv;
    }
}
