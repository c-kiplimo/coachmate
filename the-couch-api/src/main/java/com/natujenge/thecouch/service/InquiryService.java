package com.natujenge.thecouch.service;


import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.Inquiry;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.InquiryRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.mapper.InquiryMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.service.notification.emailtemplate.InquiryUtil;
import com.natujenge.thecouch.web.rest.dto.InquiryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryMapper inquiryMapper;
    private final NotificationServiceHTTPClient notificationServiceHTTPClient;
    private final UserRepository userRepository;

    public InquiryService(InquiryRepository inquiryRepository, InquiryMapper inquiryMapper, NotificationServiceHTTPClient notificationServiceHTTPClient,
                          UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryMapper = inquiryMapper;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.userRepository = userRepository;
    }

    public InquiryDTO save(InquiryDTO inquiryDTO ){
        log.info("Request to save customer inquiry: {}", inquiryDTO);
        inquiryDTO.setCreatedAt(LocalDateTime.now());
        inquiryDTO.setEmail(inquiryDTO.getEmail());
        inquiryDTO.setName(inquiryDTO.getName());

        Inquiry inquiry = inquiryMapper.toEntity(inquiryDTO);
        inquiry = inquiryRepository.save(inquiry);

        return inquiryMapper.toDto(inquiry);
    }



public void saveAndSendEmail(InquiryDTO inquiryDTO) {
    log.info("Request support: {}", inquiryDTO);

    InquiryDTO inquiry = save(inquiryDTO);

    String destinationAddress = Constants.DEFAULT_SUPPORT_EMAIL_SOURCE_ADDRESS;
    String subject = "Inquiry Alert";
    String content = InquiryUtil.getInquiryEmailBody(inquiry);
    // Send Feedback to email
    notificationServiceHTTPClient.sendEmail(destinationAddress, subject, content, true);
}
    public void support(InquiryDTO inquiryDTO,User user) {
        log.info("Request support: {}", inquiryDTO);

        InquiryDTO inquiry = saveSupport(inquiryDTO,user);

        String destinationAddress = Constants.DEFAULT_SUPPORT_EMAIL_SOURCE_ADDRESS;
        String subject = "Inquiry Alert";
        String content = InquiryUtil.getInquiryEmailBody(inquiry);
        // Send Feedback to email
        notificationServiceHTTPClient.sendEmail(destinationAddress, subject, content, true);
    }
    public InquiryDTO saveSupport(InquiryDTO inquiryDTO, User user){
        log.info("Request to save customer inquiry: {}", inquiryDTO);
        inquiryDTO.setCreatedAt(LocalDateTime.now());
        inquiryDTO.setEmail(inquiryDTO.getEmail());
        inquiryDTO.setName(inquiryDTO.getName());

        Inquiry inquiry = inquiryMapper.toEntity(inquiryDTO);
        inquiry = inquiryRepository.save(inquiry);

        return inquiryMapper.toDto(inquiry);
    }
}
