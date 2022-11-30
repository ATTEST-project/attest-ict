package com.attest.ict.service.mapper;

import com.attest.ict.domain.AssetUGCable;
import com.attest.ict.service.dto.AssetUGCableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetUGCable} and its DTO {@link AssetUGCableDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface AssetUGCableMapper extends EntityMapper<AssetUGCableDTO, AssetUGCable> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    AssetUGCableDTO toDto(AssetUGCable s);
}
