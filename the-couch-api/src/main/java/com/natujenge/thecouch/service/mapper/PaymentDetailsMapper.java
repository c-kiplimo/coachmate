package ke.natujenge.baked.service.mapper;

import ke.natujenge.baked.domain.BakerPaymentDetails;
import ke.natujenge.baked.service.dto.BakerPaymentDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface BakerPaymentDetailsMapper extends EntityMapper<BakerPaymentDetailsDTO, BakerPaymentDetails>{

    @Mapping(target = "baker", source = "baker")
    BakerPaymentDetailsDTO toDto(BakerPaymentDetails b);
}
