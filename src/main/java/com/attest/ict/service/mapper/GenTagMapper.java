package com.attest.ict.service.mapper;

import com.attest.ict.domain.GenTag;
import com.attest.ict.service.dto.GenTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GenTag} and its DTO {@link GenTagDTO}.
 */
@Mapper(componentModel = "spring", uses = { GeneratorMapper.class })
public interface GenTagMapper extends EntityMapper<GenTagDTO, GenTag> {
    @Mapping(target = "generator", source = "generator", qualifiedByName = "busNum")
    GenTagDTO toDto(GenTag s);
}
