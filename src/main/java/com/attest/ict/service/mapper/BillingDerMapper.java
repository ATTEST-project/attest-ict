package com.attest.ict.service.mapper;

import com.attest.ict.domain.BillingDer;
import com.attest.ict.service.dto.BillingDerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillingDer} and its DTO {@link BillingDerDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface BillingDerMapper extends EntityMapper<BillingDerDTO, BillingDer> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BillingDerDTO toDto(BillingDer s);
}
