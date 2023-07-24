package com.natujenge.thecouch.service.mapper;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {})
public interface ClientWalletMapper extends EntityMapper<ClientWalletDTO, ClientWallet> {
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "clientFullName", source = "client.fullName")
    @Mapping(target = "clientType", source = "client.clientType")
    @Mapping(target = "clientMsisdn", source = "client.msisdn")
          @Override
    ClientWalletDTO toDto(ClientWallet entity);
}
