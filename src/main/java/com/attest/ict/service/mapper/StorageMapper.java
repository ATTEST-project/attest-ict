package com.attest.ict.service.mapper;

import com.attest.ict.domain.Storage;
import com.attest.ict.service.dto.StorageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Storage} and its DTO {@link StorageDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface StorageMapper extends EntityMapper<StorageDTO, Storage> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    StorageDTO toDto(Storage s);
}
