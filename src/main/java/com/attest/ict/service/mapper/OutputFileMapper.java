package com.attest.ict.service.mapper;

import com.attest.ict.domain.OutputFile;
import com.attest.ict.service.dto.OutputFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutputFile} and its DTO {@link OutputFileDTO}.
 */
@Mapper(componentModel = "spring", uses = { ToolMapper.class, NetworkMapper.class, SimulationMapper.class })
public interface OutputFileMapper extends EntityMapper<OutputFileDTO, OutputFile> {
    @Mapping(target = "tool", source = "tool", qualifiedByName = "id")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    @Mapping(target = "simulation", source = "simulation", qualifiedByName = "uuid")
    OutputFileDTO toDto(OutputFile s);
}
