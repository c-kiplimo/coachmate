package com.natujenge.thecouch.service.mapper;


import com.natujenge.thecouch.service.dto.NotificationSettingsDTO;
import com.natujenge.thecouch.domain.NotificationSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface NotificationSettingsMapper extends EntityMapper<NotificationSettingsDTO, NotificationSettings>{

    @Mapping(target = "coach", source = "coach")
    NotificationSettingsDTO toDto(NotificationSettings b);
}
