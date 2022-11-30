package com.attest.ict.service.mapper;

import com.attest.ict.domain.TopologyBus;
import com.attest.ict.service.dto.TopologyBusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopologyBus} and its DTO {@link TopologyBusDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface TopologyBusMapper extends EntityMapper<TopologyBusDTO, TopologyBus> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    TopologyBusDTO toDto(TopologyBus s);

    @Named("powerLineBranch")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "powerLineBranch", source = "powerLineBranch")
    TopologyBusDTO toDtoPowerLineBranch(TopologyBus topologyBus);
}
