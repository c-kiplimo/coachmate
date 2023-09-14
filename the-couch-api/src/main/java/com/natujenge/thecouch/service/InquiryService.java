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

    public InquiryDTO save(InquiryDTO inquiryDTO, User user){
        log.info("Request to save customer inquiry: {}", inquiryDTO);
        inquiryDTO.setCreatedAt(LocalDateTime.now());
        inquiryDTO.setEmail(user.getEmail());
        inquiryDTO.setName(user.getFullName());

        Inquiry inquiry = inquiryMapper.toEntity(inquiryDTO);
        inquiry = inquiryRepository.save(inquiry);

        return inquiryMapper.toDto(inquiry);
    }

    public void saveAndSendEmail(InquiryDTO inquiryDTO, Long clientId) {
        log.info("Request to save and send  inquiry email: {}", inquiryDTO);
        User user = userRepository.findById(clientId).get();

        InquiryDTO inquiry = save(inquiryDTO, user);

        String destinationAddress = Constants.DEFAULT_SUPPORT_EMAIL_SOURCE_ADDRESS;
        String subject = "Inquiry Alert";
        String content = InquiryUtil.getInquiryEmailBody(inquiry);
        // Send Feedback to email
        notificationServiceHTTPClient.sendEmail(destinationAddress, subject, content, true);
    }
}
