package com.attest.ict.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ToolsConfiguration.class)
@TestPropertySource("classpath:tools-path-config-test.properties")
public class ToolsConfigurationTest {

    @Autowired
    private ToolsConfiguration toolsConfig;

    @Test
    void testGetToolsPathSet() {
        assertThat("/ATTEST/Tools/").isEqualTo(toolsConfig.getPath());
    }
}
