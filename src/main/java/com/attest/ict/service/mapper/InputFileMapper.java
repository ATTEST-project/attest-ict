package com.attest.ict.service.mapper;

import com.attest.ict.domain.InputFile;
import com.attest.ict.service.dto.InputFileDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InputFile} and its DTO {@link InputFileDTO}.
 */
@Mapper(componentModel = "spring", uses = { ToolMapper.class, NetworkMapper.class })
public interface InputFileMapper extends EntityMapper<InputFileDTO, InputFile> {
    @Mapping(target = "tool", source = "tool", qualifiedByName = "id")
    @Mapping(target = "network", source = "network", qualifiedByName = "id")
    InputFileDTO toDto(InputFile s);

    @Named("fileName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fileName", source = "fileName")
    InputFileDTO toDtoFileName(InputFile inputFile);

    @Named("fileNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fileName", source = "fileName")
    Set<InputFileDTO> toDtoFileNameSet(Set<InputFile> inputFile);
}
