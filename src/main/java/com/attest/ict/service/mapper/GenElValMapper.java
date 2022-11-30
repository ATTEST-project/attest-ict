package com.attest.ict.service.mapper;

import com.attest.ict.domain.GenElVal;
import com.attest.ict.service.dto.GenElValDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GenElVal} and its DTO {@link GenElValDTO}.
 */
@Mapper(componentModel = "spring", uses = { GenProfileMapper.class, GeneratorMapper.class })
public interface GenElValMapper extends EntityMapper<GenElValDTO, GenElVal> {
    @Mapping(target = "genProfile", source = "genProfile", qualifiedByName = "id")
    @Mapping(target = "generator", source = "generator", qualifiedByName = "id")
    GenElValDTO toDto(GenElVal s);
}
