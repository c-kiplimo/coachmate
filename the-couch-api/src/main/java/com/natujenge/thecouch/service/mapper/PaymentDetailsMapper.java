package com.natujenge.thecouch.service.mapper;


import com.natujenge.thecouch.domain.PaymentDetails;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import com.natujenge.thecouch.web.rest.dto.PaymentDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentDetailsMapper extends EntityMapper<PaymentDetailsDTO, PaymentDetails>{

//    @Mapping(target = "coach", source = "coach")
//    @Mapping(target = "organization", source = "organization")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "createdBy", source = "createdBy")
//    @Mapping(target = "lastUpdatedAt", source = "lastUpdatedAt")
//    @Mapping(target = "lastUpdatedBy", source = "lastUpdatedBy")

    @Override
    PaymentDetailsDTO toDto(PaymentDetails entity);

}
