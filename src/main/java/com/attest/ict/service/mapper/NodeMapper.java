package com.attest.ict.service.mapper;

import com.attest.ict.domain.Node;
import com.attest.ict.service.dto.NodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Node} and its DTO {@link NodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodeMapper extends EntityMapper<NodeDTO, Node> {}
