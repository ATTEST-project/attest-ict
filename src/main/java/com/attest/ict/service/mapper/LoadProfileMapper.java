package com.attest.ict.service.mapper;

import com.attest.ict.domain.LoadProfile;
import com.attest.ict.service.dto.LoadProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoadProfile} and its DTO {@link LoadProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { InputFileMapper.class, NetworkMapper.class })
public interface LoadProfileMapper extends EntityMapper<LoadProfileDTO, LoadProfile> {
    @Mapping(target = "inputFile", source = "inputFile", qualifiedByName = "fileName")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    LoadProfileDTO toDto(LoadProfile s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoadProfileDTO toDtoId(LoadProfile loadProfile);
}
