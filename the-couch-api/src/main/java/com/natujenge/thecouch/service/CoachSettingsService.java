package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachSettings;
import com.natujenge.thecouch.repository.CoachSettingsRepository;
import com.natujenge.thecouch.security.SecurityUtils;
import com.natujenge.thecouch.service.mapper.CoachSettingsMapper;
import com.natujenge.thecouch.web.rest.dto.CoachSettingsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CoachSettingsService {
    private final CoachSettingsRepository coachSettingsRepository;
    private final CoachSettingsMapper coachSettingsMapper;

    public CoachSettingsService(CoachSettingsRepository coachSettingsRepository, CoachSettingsMapper coachSettingsMapper) {
        this.coachSettingsRepository = coachSettingsRepository;
        this.coachSettingsMapper = coachSettingsMapper;
    }

    public CoachSettingsDTO save(CoachSettingsDTO coachSettingsDTO){
        log.info("Request to save BakerSettings: {}", coachSettingsDTO);
        if (coachSettingsDTO.getId() == null){
            coachSettingsDTO.setCreatedAt(LocalDateTime.now());
            coachSettingsDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
        } else {
            coachSettingsDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
            coachSettingsDTO.setLastUpdatedAt(LocalDateTime.now());
        }

        CoachSettings coachPaymentDetails = coachSettingsMapper.toEntity(coachSettingsDTO);
        coachPaymentDetails= coachSettingsRepository.save(coachPaymentDetails);

        return coachSettingsMapper.toDto(coachPaymentDetails);
    }

    public CoachSettingsDTO findByCoachId(Long coachId){
        log.info("Request to find coach settings by coachId:{}", coachId);

        return coachSettingsMapper.toDto(coachSettingsRepository.findByCoachId(coachId));
    }


    public CoachSettingsDTO findTopByCoachId(Long coachId){
        log.info("Request to find top coach settings by coachId:{}", coachId);

        return coachSettingsMapper.toDto(coachSettingsRepository.findTopByCoachId(coachId));
    }

}
