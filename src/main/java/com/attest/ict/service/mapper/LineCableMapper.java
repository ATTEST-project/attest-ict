package com.attest.ict.service.mapper;

import com.attest.ict.domain.LineCable;
import com.attest.ict.service.dto.LineCableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LineCable} and its DTO {@link LineCableDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface LineCableMapper extends EntityMapper<LineCableDTO, LineCable> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    LineCableDTO toDto(LineCable s);
}
