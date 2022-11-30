package com.attest.ict.service.mapper;

import com.attest.ict.domain.BusName;
import com.attest.ict.service.dto.BusNameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusName} and its DTO {@link BusNameDTO}.
 */
@Mapper(componentModel = "spring", uses = { BusMapper.class })
public interface BusNameMapper extends EntityMapper<BusNameDTO, BusName> {
    @Mapping(target = "bus", source = "bus", qualifiedByName = "busNum")
    BusNameDTO toDto(BusName s);
}
