package com.attest.ict.service.mapper;

import com.attest.ict.domain.StorageCost;
import com.attest.ict.service.dto.StorageCostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StorageCost} and its DTO {@link StorageCostDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StorageCostMapper extends EntityMapper<StorageCostDTO, StorageCost> {}
