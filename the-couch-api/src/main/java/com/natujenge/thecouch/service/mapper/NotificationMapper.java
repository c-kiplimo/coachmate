package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.web.rest.dto.NotificationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification>{
    @Override
    NotificationDTO toDto(Notification entity);
}
