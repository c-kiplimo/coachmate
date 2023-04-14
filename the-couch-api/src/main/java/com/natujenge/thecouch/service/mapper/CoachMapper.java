package ke.natujenge.baked.service.mapper;

import ke.natujenge.baked.domain.Baker;
import ke.natujenge.baked.domain.BakerWallet;
import ke.natujenge.baked.service.dto.BakerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface BakerMapper extends EntityMapper<BakerDTO, Baker>{

    @Mapping(target = "bakerNotificationSettings", source = "bakerNotificationSettings")
    BakerDTO toDto(Baker b);

    default Baker fromId(Long id) {
        if (id == null) {
            return null;
        }
        Baker baker = new Baker();
        baker.setId(id);
        return baker;
    }
}
