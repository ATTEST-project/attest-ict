package com.attest.ict.service.mapper;

import com.attest.ict.domain.FlexCost;
import com.attest.ict.service.dto.FlexCostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlexCost} and its DTO {@link FlexCostDTO}.
 */
@Mapper(componentModel = "spring", uses = { FlexProfileMapper.class })
public interface FlexCostMapper extends EntityMapper<FlexCostDTO, FlexCost> {
    @Mapping(target = "flexProfile", source = "flexProfile", qualifiedByName = "id")
    FlexCostDTO toDto(FlexCost s);
}
