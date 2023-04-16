package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.service.dto.CoachDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface CoachMapper extends EntityMapper<CoachDTO, Coach>{

    CoachDTO toDto(Coach b);

    default Coach fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coach coach = new Coach();
        coach.setId(id);
        return coach;
    }
}
