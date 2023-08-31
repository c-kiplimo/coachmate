package com.natujenge.thecouch.service.mapper;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.web.rest.dto.ContractDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Override
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.orgName")
    @Mapping(target = "organizationMsisdn", source = "organization.msisdn")
    @Mapping(target = "organizationEmail", source = "organization.email")
    @Mapping(target = "organizationSuperCoachId", source = "organization.superCoachId")
    @Mapping(target = "organizationStatus", source = "organization.status")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientFullName", source = "client.fullName")
    @Mapping(target = "clientFirstName", source = "client.firstName")
    @Mapping(target = "clientLastName", source = "client.lastName")
    @Mapping(target = "clientEmail", source = "client.email")
    @Mapping(target = "clientBusinessName", source = "client.businessName")
    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "coachFullName", source = "coach.fullName")
    @Mapping(target = "coachFirstName", source = "coach.firstName")
    @Mapping(target = "coachLastName", source = "coach.lastName")
    @Mapping(target = "coachBusinessName", source = "coach.businessName")
    @Mapping(target = "coachEmail", source = "coach.email")

    ContractDTO toDto(Contract Entity);
}
