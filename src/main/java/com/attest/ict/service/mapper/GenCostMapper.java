package com.attest.ict.service.mapper;

import com.attest.ict.domain.GenCost;
import com.attest.ict.service.dto.GenCostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GenCost} and its DTO {@link GenCostDTO}.
 */
@Mapper(componentModel = "spring", uses = { GeneratorMapper.class })
public interface GenCostMapper extends EntityMapper<GenCostDTO, GenCost> {
    @Mapping(target = "generator", source = "generator", qualifiedByName = "busNum")
    GenCostDTO toDto(GenCost s);
}
