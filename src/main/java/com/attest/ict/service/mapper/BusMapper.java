package com.attest.ict.service.mapper;

import com.attest.ict.domain.Bus;
import com.attest.ict.service.dto.BusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bus} and its DTO {@link BusDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface BusMapper extends EntityMapper<BusDTO, Bus> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BusDTO toDto(Bus s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BusDTO toDtoId(Bus bus);

    @Named("busNum")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "busNum", source = "busNum")
    BusDTO toDtoBusNum(Bus bus);
}
