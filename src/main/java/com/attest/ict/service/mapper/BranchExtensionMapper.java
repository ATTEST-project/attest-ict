package com.attest.ict.service.mapper;

import com.attest.ict.domain.BranchExtension;
import com.attest.ict.service.dto.BranchExtensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchExtension} and its DTO {@link BranchExtensionDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class })
public interface BranchExtensionMapper extends EntityMapper<BranchExtensionDTO, BranchExtension> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    BranchExtensionDTO toDto(BranchExtension s);
}
