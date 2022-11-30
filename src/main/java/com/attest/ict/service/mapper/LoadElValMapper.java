package com.attest.ict.service.mapper;

import com.attest.ict.domain.LoadElVal;
import com.attest.ict.service.dto.LoadElValDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoadElVal} and its DTO {@link LoadElValDTO}.
 */
@Mapper(componentModel = "spring", uses = { LoadProfileMapper.class, BusMapper.class })
public interface LoadElValMapper extends EntityMapper<LoadElValDTO, LoadElVal> {
    @Mapping(target = "loadProfile", source = "loadProfile", qualifiedByName = "id")
    @Mapping(target = "bus", source = "bus", qualifiedByName = "id")
    LoadElValDTO toDto(LoadElVal s);
}
