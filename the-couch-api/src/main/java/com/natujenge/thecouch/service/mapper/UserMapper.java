package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {
    UserMapper  instance = Mappers.getMapper(UserMapper.class);
    UserDTO toDto(User user);
}
