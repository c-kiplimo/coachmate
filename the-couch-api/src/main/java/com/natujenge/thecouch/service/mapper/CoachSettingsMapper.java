package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.CoachSettings;
import com.natujenge.thecouch.web.rest.dto.CoachSettingsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CoachSettingsMapper extends EntityMapper<CoachSettingsDTO, CoachSettings>{

    CoachSettingsDTO toDto(CoachSettings b);
}
