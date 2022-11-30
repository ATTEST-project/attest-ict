package com.attest.ict.service.mapper;

import com.attest.ict.domain.TransfElVal;
import com.attest.ict.service.dto.TransfElValDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransfElVal} and its DTO {@link TransfElValDTO}.
 */
@Mapper(componentModel = "spring", uses = { TransfProfileMapper.class, BranchMapper.class })
public interface TransfElValMapper extends EntityMapper<TransfElValDTO, TransfElVal> {
    @Mapping(target = "transfProfile", source = "transfProfile", qualifiedByName = "id")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    TransfElValDTO toDto(TransfElVal s);
}
