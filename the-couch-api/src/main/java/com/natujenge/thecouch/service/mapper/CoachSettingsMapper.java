package ke.natujenge.baked.service.mapper;

import ke.natujenge.baked.domain.BakerSettings;
import ke.natujenge.baked.service.dto.BakerSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface BakerSettingsMapper extends EntityMapper<BakerSettingsDTO, BakerSettings>{

    @Mapping(target = "baker", source = "baker")
    BakerSettingsDTO toDto(BakerSettings b);
}
