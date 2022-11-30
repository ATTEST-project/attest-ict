package com.attest.ict.service.mapper;

import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.service.dto.ToolLogFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolLogFile} and its DTO {@link ToolLogFileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ToolLogFileMapper extends EntityMapper<ToolLogFileDTO, ToolLogFile> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ToolLogFileDTO toDtoId(ToolLogFile toolLogFile);
}
