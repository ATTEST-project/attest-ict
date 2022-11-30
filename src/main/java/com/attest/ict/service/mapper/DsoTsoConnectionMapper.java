package com.attest.ict.service.mapper;

import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.service.dto.DsoTsoConnectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DsoTsoConnection} and its DTO {@link DsoTsoConnectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface DsoTsoConnectionMapper extends EntityMapper<DsoTsoConnectionDTO, DsoTsoConnection> {
    @Mapping(target = "dsoNetwork", source = "dsoNetwork", qualifiedByName = "id")
    DsoTsoConnectionDTO toDto(DsoTsoConnection s);
}
