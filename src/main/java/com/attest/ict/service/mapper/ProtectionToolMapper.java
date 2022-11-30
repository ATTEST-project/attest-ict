package com.attest.ict.service.mapper;

import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.service.dto.ProtectionToolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProtectionTool} and its DTO {@link ProtectionToolDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class, BusMapper.class })
public interface ProtectionToolMapper extends EntityMapper<ProtectionToolDTO, ProtectionTool> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    @Mapping(target = "bus", source = "bus", qualifiedByName = "id")
    ProtectionToolDTO toDto(ProtectionTool s);
}
