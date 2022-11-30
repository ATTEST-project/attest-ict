package com.attest.ict.service.mapper;

import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.service.dto.GeneratorExtensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GeneratorExtension} and its DTO {@link GeneratorExtensionDTO}.
 */
@Mapper(componentModel = "spring", uses = { GeneratorMapper.class })
public interface GeneratorExtensionMapper extends EntityMapper<GeneratorExtensionDTO, GeneratorExtension> {
    @Mapping(target = "generator", source = "generator", qualifiedByName = "busNum")
    GeneratorExtensionDTO toDto(GeneratorExtension s);
}
