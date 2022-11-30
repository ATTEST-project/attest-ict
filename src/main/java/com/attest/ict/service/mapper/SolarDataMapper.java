package com.attest.ict.service.mapper;

import com.attest.ict.domain.SolarData;
import com.attest.ict.service.dto.SolarDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SolarData} and its DTO {@link SolarDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SolarDataMapper extends EntityMapper<SolarDataDTO, SolarData> {}
