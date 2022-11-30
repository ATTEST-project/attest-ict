package com.attest.ict.service.mapper;

import com.attest.ict.domain.BranchProfile;
import com.attest.ict.service.dto.BranchProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchProfile} and its DTO {@link BranchProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { InputFileMapper.class, NetworkMapper.class })
public interface BranchProfileMapper extends EntityMapper<BranchProfileDTO, BranchProfile> {
    @Mapping(target = "inputFile", source = "inputFile", qualifiedByName = "fileName")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BranchProfileDTO toDto(BranchProfile s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchProfileDTO toDtoId(BranchProfile branchProfile);
}
