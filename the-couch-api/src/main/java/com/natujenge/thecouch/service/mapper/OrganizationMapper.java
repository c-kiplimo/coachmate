package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.web.rest.dto.OrganizationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {
    OrganizationDTO toDto(Organization organization);
}
