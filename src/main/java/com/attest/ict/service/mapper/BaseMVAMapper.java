package com.attest.ict.service.mapper;

import com.attest.ict.domain.BaseMVA;
import com.attest.ict.service.dto.BaseMVADTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaseMVA} and its DTO {@link BaseMVADTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface BaseMVAMapper extends EntityMapper<BaseMVADTO, BaseMVA> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BaseMVADTO toDto(BaseMVA s);
}
