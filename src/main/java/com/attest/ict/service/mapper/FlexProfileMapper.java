package com.attest.ict.service.mapper;

import com.attest.ict.domain.FlexProfile;
import com.attest.ict.service.dto.FlexProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlexProfile} and its DTO {@link FlexProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { InputFileMapper.class, NetworkMapper.class })
public interface FlexProfileMapper extends EntityMapper<FlexProfileDTO, FlexProfile> {
    @Mapping(target = "inputFile", source = "inputFile", qualifiedByName = "fileName")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    FlexProfileDTO toDto(FlexProfile s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FlexProfileDTO toDtoId(FlexProfile flexProfile);
}
