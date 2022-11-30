package com.attest.ict.service.mapper;

import com.attest.ict.domain.Topology;
import com.attest.ict.service.dto.TopologyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Topology} and its DTO {@link TopologyDTO}.
 */
@Mapper(componentModel = "spring", uses = { TopologyBusMapper.class })
public interface TopologyMapper extends EntityMapper<TopologyDTO, Topology> {
    @Mapping(target = "powerLineBranchParent", source = "powerLineBranchParent", qualifiedByName = "powerLineBranch")
    TopologyDTO toDto(Topology s);
}
