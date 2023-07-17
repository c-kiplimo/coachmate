package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.web.rest.dto.CoachEducationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CoachEducationMapper extends EntityMapper<CoachEducationDTO, CoachEducation> {
    CoachEducationDTO toDto(CoachEducation Entity);
}
