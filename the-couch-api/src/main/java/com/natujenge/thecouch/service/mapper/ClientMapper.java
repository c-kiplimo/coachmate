package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.web.rest.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper extends EntityMapper<ClientDTO, User> {
    @Override
    ClientDTO toDto(User Entity);
}
