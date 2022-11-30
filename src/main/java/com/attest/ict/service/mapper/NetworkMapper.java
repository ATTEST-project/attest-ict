package com.attest.ict.service.mapper;

import com.attest.ict.domain.Network;
import com.attest.ict.service.dto.NetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Network} and its DTO {@link NetworkDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NetworkMapper extends EntityMapper<NetworkDTO, Network> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NetworkDTO toDtoId(Network network);
}
