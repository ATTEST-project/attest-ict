package com.attest.ict.service.mapper;

import com.attest.ict.domain.Branch;
import com.attest.ict.service.dto.BranchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Branch} and its DTO {@link BranchDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface BranchMapper extends EntityMapper<BranchDTO, Branch> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BranchDTO toDto(Branch s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoId(Branch branch);
}
