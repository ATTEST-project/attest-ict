package com.attest.ict.service.mapper;

import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.service.dto.VoltageLevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VoltageLevel} and its DTO {@link VoltageLevelDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface VoltageLevelMapper extends EntityMapper<VoltageLevelDTO, VoltageLevel> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    VoltageLevelDTO toDto(VoltageLevel s);
}
