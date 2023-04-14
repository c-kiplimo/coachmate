package ke.natujenge.baked.service.mapper;

import ke.natujenge.baked.domain.BakerNotificationSettings;
import ke.natujenge.baked.service.dto.BakerNotificationSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface BakerNotificationSettingsMapper extends EntityMapper<BakerNotificationSettingsDTO, BakerNotificationSettings>{

    @Mapping(target = "baker", source = "baker")
    BakerNotificationSettingsDTO toDto(BakerNotificationSettings b);
}
