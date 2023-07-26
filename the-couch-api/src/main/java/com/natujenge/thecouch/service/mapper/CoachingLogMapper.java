package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.CoachingLog;
import com.natujenge.thecouch.web.rest.dto.CoachingLogDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CoachingLogMapper extends EntityMapper<CoachingLogDTO, CoachingLog>{
    CoachingLogDTO toDto(CoachingLog Entity);
}
