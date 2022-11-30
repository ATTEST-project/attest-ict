package com.attest.ict.service.mapper;

import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CapacitorBankData} and its DTO {@link CapacitorBankDataDTO}.
 */
@Mapper(componentModel = "spring", uses = { NetworkMapper.class })
public interface CapacitorBankDataMapper extends EntityMapper<CapacitorBankDataDTO, CapacitorBankData> {
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    CapacitorBankDataDTO toDto(CapacitorBankData s);
}
