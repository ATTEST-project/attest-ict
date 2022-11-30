package com.attest.ict.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.service.OdsNetworkService;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ToolsConfiguration.class)
@TestPropertySource("classpath:tools-path-config-test.properties")
public class T41ServiceImplTest {

    private OdsNetworkService t41Service;
    private final Long NETWORK_ID = Long.valueOf(11);
}
