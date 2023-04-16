package com.natujenge.thecouch.service.mapper;


import com.natujenge.thecouch.service.dto.PaymentDetailsDTO;
import com.natujenge.thecouch.domain.PaymentDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentDetailsMapper extends EntityMapper<PaymentDetailsDTO, PaymentDetails>{

//    @Mapping(target = "coachId", source = "coach.id")
//    @Mapping(target = "organizationId", source = "organization.id")
    PaymentDetailsDTO toDto(PaymentDetails b);
}
