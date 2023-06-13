package com.natujenge.thecouch.service.mapper;


import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.web.rest.dto.NotificationSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface NotificationSettingsMapper extends EntityMapper<NotificationSettingsDTO, NotificationSettings>{

    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "organizationId", source = "organization.id")

    NotificationSettingsDTO toDto(NotificationSettings b);
}
