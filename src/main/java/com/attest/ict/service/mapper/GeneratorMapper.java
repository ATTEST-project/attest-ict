package com.attest.ict.service.mapper;

import com.attest.ict.domain.Generator;
import com.attest.ict.service.dto.GeneratorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Generator} and its DTO {@link GeneratorDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface GeneratorMapper extends EntityMapper<GeneratorDTO, Generator> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    GeneratorDTO toDto(Generator s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GeneratorDTO toDtoId(Generator generator);

    @Named("busNum")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "busNum", source = "busNum")
    GeneratorDTO toDtoBusNum(Generator generator);
}
