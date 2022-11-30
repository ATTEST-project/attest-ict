package com.attest.ict.service.mapper;

import com.attest.ict.domain.Simulation;
import com.attest.ict.service.dto.SimulationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Simulation} and its DTO {@link SimulationDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class, InputFileMapper.class })
public interface SimulationMapper extends EntityMapper<SimulationDTO, Simulation> {
    //@Mapping(target = "network", source = "network", qualifiedByName = "id")
    @Mapping(target = "inputFiles", source = "inputFiles", qualifiedByName = "fileNameSet")
    SimulationDTO toDto(Simulation s);

    @Mapping(target = "removeInputFile", ignore = true)
    Simulation toEntity(SimulationDTO simulationDTO);

    @Named("uuid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "uuid", source = "uuid")
    SimulationDTO toDtoUuid(Simulation simulation);
}
