package com.attest.ict.service.mapper;

import com.attest.ict.domain.Transformer;
import com.attest.ict.service.dto.TransformerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transformer} and its DTO {@link TransformerDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface TransformerMapper extends EntityMapper<TransformerDTO, Transformer> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    TransformerDTO toDto(Transformer s);
}
