package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.web.rest.dto.SessionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface SessionMapper extends EntityMapper<SessionDTO, Session>  {
    @Mapping(target = "sessionSchedulesId", source = "sessionSchedules.id")
    @Mapping(target = "sessionSchedulesOrgId", source = "sessionSchedules.orgId")
    @Mapping(target = "sessionSchedulesSessionDate", source = "sessionSchedules.sessionDate")
   // @Mapping(target = "sessionDate", source = "session.sessionDate")
    @Mapping(target = "sessionSchedulesStartTime", source = "sessionSchedules.startTime")
    @Mapping(target = "sessionSchedulesEndTime", source = "sessionSchedules.endTime")
    @Mapping(target = "sessionSchedulesBooked", source = "sessionSchedules.booked")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientFullName", source = "client.fullName")
    @Mapping(target = "clientType", source = "client.clientType")
    @Mapping(target = "clientMsisdn", source = "client.msisdn")
    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "contractId", source = "contract.id")
    @Mapping(target = "contractCoachingCategory", source = "contract.coachingCategory")
    @Mapping(target = "contractCoachingTopic", source = "contract.coachingTopic")

//    @Override
    SessionDTO toDto(Session entity);

}
