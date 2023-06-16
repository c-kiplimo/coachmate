package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.web.rest.dto.SessionSchedulesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface SessionSchedulesMapper extends EntityMapper<SessionSchedulesDTO, SessionSchedules>{
//    @Override
    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "coachFullName", source = "coach.fullName")
    @Mapping(target = "coachMsisdn", source = "coach.msisdn")

    SessionSchedulesDTO toDto(SessionSchedules entity);
}
