package com.attest.ict.service.mapper;

import com.attest.ict.domain.GenProfile;
import com.attest.ict.service.dto.GenProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GenProfile} and its DTO {@link GenProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { InputFileMapper.class, NetworkMapper.class })
public interface GenProfileMapper extends EntityMapper<GenProfileDTO, GenProfile> {
    @Mapping(target = "inputFile", source = "inputFile", qualifiedByName = "fileName")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    GenProfileDTO toDto(GenProfile s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GenProfileDTO toDtoId(GenProfile genProfile);
}
