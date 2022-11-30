package com.attest.ict.service.mapper;

import com.attest.ict.domain.BranchElVal;
import com.attest.ict.service.dto.BranchElValDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchElVal} and its DTO {@link BranchElValDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class, BranchProfileMapper.class })
public interface BranchElValMapper extends EntityMapper<BranchElValDTO, BranchElVal> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    @Mapping(target = "branchProfile", source = "branchProfile", qualifiedByName = "id")
    BranchElValDTO toDto(BranchElVal s);
}
