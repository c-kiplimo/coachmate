package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.web.rest.dto.CoachDTO;
import com.natujenge.thecouch.web.rest.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CoachMapper extends EntityMapper<CoachDTO, User>{


    CoachDTO toDto(User Entity);


//    default User fromId(Long id) {
//        if (id == null) {
//            return null;
//        }
//        User coach = new User();
//        coach.setId(id);
//        return coach;
//    }
}
