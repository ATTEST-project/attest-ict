package com.attest.ict.service.mapper;

import com.attest.ict.domain.Task;
import com.attest.ict.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { ToolLogFileMapper.class, SimulationMapper.class, ToolMapper.class, UserMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    // @Mapping(target = "toolLogFile", source = "toolLogFile", qualifiedByName = "id")
    // @Mapping(target = "tool", source = "tool", qualifiedByName = "id")
    // @Mapping(target = "user", source = "user", qualifiedByName = "id")
    // @Mapping(target = "toolLogFile.fileName"

    // @Mapping(target = "toolLogFile.id", source = "toolLogFile.id")
    // @Mapping(target = "toolLogFile.fileName", source = "toolLogFile.fileName")
    @Mapping(target = "tool.id", source = "tool.id")
    @Mapping(target = "tool.name", source = "tool.name")
    @Mapping(target = "user.id", source = "user.id")
    @Mapping(target = "user.login", source = "user.login")
    // @Mapping(target = "simulation", source = "simulation", qualifiedByName = "uuid")

    @Mapping(target = "toolLogFileId", source = "toolLogFile.id")
    @Mapping(target = "toolLogFileName", source = "toolLogFile.fileName")
    @Mapping(target = "networkId", source = "simulation.network.id")
    @Mapping(target = "simulationId", source = "simulation.id")
    @Mapping(target = "simulationUuid", source = "simulation.uuid")
    @Mapping(target = "simulationConfigFile", source = "simulation.configFile")
    TaskDTO toDto(Task s);
}
