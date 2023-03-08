package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.Response;
import com.natujenge.thecouch.repository.ResponseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
    @Service
    @Slf4j
    public class ResponseService {
        @Autowired
        ResponseRepository responseRepository;

        public void saveResponse(Response responseRequest) {
            responseRepository.save(responseRequest);
            log.info("Response captures Successfully");
        }

    }

