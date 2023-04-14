package ke.natujenge.baked.service;

import ke.natujenge.baked.domain.BakerPaymentDetails;
import ke.natujenge.baked.domain.BakerSettings;
import ke.natujenge.baked.repository.BakerSettingsRepository;
import ke.natujenge.baked.security.SecurityUtils;
import ke.natujenge.baked.service.dto.BakerPaymentDetailsDTO;
import ke.natujenge.baked.service.dto.BakerSettingsDTO;
import ke.natujenge.baked.service.mapper.BakerSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BakerSettingsService {
    private final BakerSettingsRepository bakerSettingsRepository;
    private final BakerSettingsMapper bakerSettingsMapper;

    public BakerSettingsService(BakerSettingsRepository bakerSettingsRepository, BakerSettingsMapper bakerSettingsMapper) {
        this.bakerSettingsRepository = bakerSettingsRepository;
        this.bakerSettingsMapper = bakerSettingsMapper;
    }

    public BakerSettingsDTO save(BakerSettingsDTO bakerSettingsDTO){
        log.info("Request to save BakerSettings: {}", bakerSettingsDTO);
        if (bakerSettingsDTO.getId() == null){
            bakerSettingsDTO.setCreatedAt(LocalDateTime.now());
            bakerSettingsDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
        } else {
            bakerSettingsDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
            bakerSettingsDTO.setLastUpdatedAt(LocalDateTime.now());
        }

        BakerSettings bakerPaymentDetails = bakerSettingsMapper.toEntity(bakerSettingsDTO);
        bakerPaymentDetails= bakerSettingsRepository.save(bakerPaymentDetails);

        return bakerSettingsMapper.toDto(bakerPaymentDetails);
    }

    public BakerSettingsDTO findByBakerId(Long bakerId){
        log.info("Request to find baker settings by bakerId:{}", bakerId);

        return bakerSettingsMapper.toDto(bakerSettingsRepository.findByBakerId(bakerId));
    }

    public BakerSettingsDTO findTopByBakerId(Long bakerId){
        log.info("Request to find top baker settings by bakerId:{}", bakerId);

        return bakerSettingsMapper.toDto(bakerSettingsRepository.findTopByBakerId(bakerId));
    }
}
