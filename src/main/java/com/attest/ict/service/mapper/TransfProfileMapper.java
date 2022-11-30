package com.attest.ict.service.mapper;

import com.attest.ict.domain.TransfProfile;
import com.attest.ict.service.dto.TransfProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransfProfile} and its DTO {@link TransfProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { InputFileMapper.class, NetworkMapper.class })
public interface TransfProfileMapper extends EntityMapper<TransfProfileDTO, TransfProfile> {
    @Mapping(target = "inputFile", source = "inputFile", qualifiedByName = "fileName")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    TransfProfileDTO toDto(TransfProfile s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransfProfileDTO toDtoId(TransfProfile transfProfile);
}
