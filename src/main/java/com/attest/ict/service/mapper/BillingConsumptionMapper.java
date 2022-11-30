package com.attest.ict.service.mapper;

import com.attest.ict.domain.BillingConsumption;
import com.attest.ict.service.dto.BillingConsumptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillingConsumption} and its DTO {@link BillingConsumptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface BillingConsumptionMapper extends EntityMapper<BillingConsumptionDTO, BillingConsumption> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    BillingConsumptionDTO toDto(BillingConsumption s);
}
