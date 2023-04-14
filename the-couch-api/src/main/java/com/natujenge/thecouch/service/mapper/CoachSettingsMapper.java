package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.CoachSettings;
import com.natujenge.thecouch.service.dto.CoachSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface CoachSettingsMapper extends EntityMapper<CoachSettingsDTO, CoachSettings>{

    @Mapping(target = "coach", source = "coach")
    CoachSettingsDTO toDto(CoachSettings b);
}
