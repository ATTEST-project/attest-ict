package com.attest.ict.service.mapper;

import com.attest.ict.domain.WindData;
import com.attest.ict.service.dto.WindDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WindData} and its DTO {@link WindDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WindDataMapper extends EntityMapper<WindDataDTO, WindData> {}
