package com.attest.ict.service.mapper;

import com.attest.ict.domain.AssetTransformer;
import com.attest.ict.service.dto.AssetTransformerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetTransformer} and its DTO {@link AssetTransformerDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface AssetTransformerMapper extends EntityMapper<AssetTransformerDTO, AssetTransformer> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    AssetTransformerDTO toDto(AssetTransformer s);
}
