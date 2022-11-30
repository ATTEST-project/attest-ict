package com.attest.ict.service.mapper;

import com.attest.ict.domain.ToolParameter;
import com.attest.ict.service.dto.ToolParameterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolParameter} and its DTO {@link ToolParameterDTO}.
 */
@Mapper(componentModel = "spring", uses = { ToolMapper.class })
public interface ToolParameterMapper extends EntityMapper<ToolParameterDTO, ToolParameter> {
    @Mapping(target = "tool", source = "tool", qualifiedByName = "num")
    ToolParameterDTO toDto(ToolParameter s);
}
