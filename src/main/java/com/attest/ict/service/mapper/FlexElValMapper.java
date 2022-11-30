package com.attest.ict.service.mapper;

import com.attest.ict.domain.FlexElVal;
import com.attest.ict.service.dto.FlexElValDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlexElVal} and its DTO {@link FlexElValDTO}.
 */
@Mapper(componentModel = "spring", uses = { FlexProfileMapper.class })
public interface FlexElValMapper extends EntityMapper<FlexElValDTO, FlexElVal> {
    @Mapping(target = "flexProfile", source = "flexProfile", qualifiedByName = "id")
    FlexElValDTO toDto(FlexElVal s);
}
