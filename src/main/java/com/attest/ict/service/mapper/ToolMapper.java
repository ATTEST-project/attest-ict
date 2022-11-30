package com.attest.ict.service.mapper;

import com.attest.ict.domain.Tool;
import com.attest.ict.service.dto.ToolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tool} and its DTO {@link ToolDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ToolMapper extends EntityMapper<ToolDTO, Tool> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ToolDTO toDtoId(Tool tool);

    @Named("num")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "num", source = "num")
    ToolDTO toDtoNum(Tool tool);
}
