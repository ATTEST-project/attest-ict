package com.attest.ict.service.mapper;

import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.service.dto.BusCoordinateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusCoordinate} and its DTO {@link BusCoordinateDTO}.
 */
@Mapper(componentModel = "spring", uses = { BusMapper.class })
public interface BusCoordinateMapper extends EntityMapper<BusCoordinateDTO, BusCoordinate> {
    @Mapping(target = "bus", source = "bus", qualifiedByName = "busNum")
    BusCoordinateDTO toDto(BusCoordinate s);
}
