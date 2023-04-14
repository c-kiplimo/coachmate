package ke.natujenge.baked.service;

import ke.natujenge.baked.domain.BakerPaymentDetails;
import ke.natujenge.baked.repository.BakerPaymentDetailsRepository;
import ke.natujenge.baked.security.SecurityUtils;
import ke.natujenge.baked.service.dto.BakerPaymentDetailsDTO;
import ke.natujenge.baked.service.mapper.BakerPaymentDetailsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BakerPaymentDetailsService {
    private final BakerPaymentDetailsRepository bakerPaymentDetailsRepository;
    private final BakerPaymentDetailsMapper bakerPaymentDetailsMapper;

    public BakerPaymentDetailsService(BakerPaymentDetailsRepository bakerPaymentDetailsRepository, BakerPaymentDetailsMapper bakerPaymentDetailsMapper) {
        this.bakerPaymentDetailsRepository = bakerPaymentDetailsRepository;
        this.bakerPaymentDetailsMapper = bakerPaymentDetailsMapper;
    }


    public BakerPaymentDetailsDTO save(BakerPaymentDetailsDTO bakerPaymentDetailsDTO){
        log.info("Request to save paymentDetails: {}", bakerPaymentDetailsDTO);
        if (bakerPaymentDetailsDTO.getId() == null){
            bakerPaymentDetailsDTO.setCreatedAt(LocalDateTime.now());
            bakerPaymentDetailsDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
        } else {
            bakerPaymentDetailsDTO.setLastUpdatedAt(LocalDateTime.now());
            bakerPaymentDetailsDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
        }

        BakerPaymentDetails bakerPaymentDetails = bakerPaymentDetailsMapper.toEntity(bakerPaymentDetailsDTO);
        bakerPaymentDetails= bakerPaymentDetailsRepository.save(bakerPaymentDetails);

        return bakerPaymentDetailsMapper.toDto(bakerPaymentDetails);
    }
    public BakerPaymentDetailsDTO findTopByBakerId(Long bakerId){
        log.info("Request to find one payment details by bakerId:{}", bakerId);

        return bakerPaymentDetailsMapper.toDto(bakerPaymentDetailsRepository.findTopByBakerId(bakerId));
    }
}
