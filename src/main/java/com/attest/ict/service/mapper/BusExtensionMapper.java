package com.attest.ict.service.mapper;

import com.attest.ict.domain.BusExtension;
import com.attest.ict.service.dto.BusExtensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusExtension} and its DTO {@link BusExtensionDTO}.
 */
@Mapper(componentModel = "spring", uses = { BusMapper.class })
public interface BusExtensionMapper extends EntityMapper<BusExtensionDTO, BusExtension> {
    @Mapping(target = "bus", source = "bus", qualifiedByName = "busNum")
    BusExtensionDTO toDto(BusExtension s);
}
