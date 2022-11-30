package com.attest.ict.service.mapper;

import com.attest.ict.domain.Price;
import com.attest.ict.service.dto.PriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {}
