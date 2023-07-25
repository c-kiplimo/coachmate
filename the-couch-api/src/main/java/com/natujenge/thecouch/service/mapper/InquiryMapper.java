package com.natujenge.thecouch.service.mapper;


import com.natujenge.thecouch.domain.Inquiry;
import com.natujenge.thecouch.web.rest.dto.InquiryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface InquiryMapper extends EntityMapper<InquiryDTO, Inquiry>{

}
