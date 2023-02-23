package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Feedback;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.FeedbackService;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Slf4j
public class FeedbackResource {

    @Autowired
    FeedbackService feedBackService;

    // create feedback
    @PostMapping
    ResponseEntity<?> createNewFeedBack(@RequestBody Feedback feedback,
                                        @RequestParam(name = "sessionId",required = false) Long sessionId,
                                        @RequestParam(name = "coachId") Long coachId,

                                        @RequestParam(name = "orgIdId", required = false) Long orgIdId,

                                        @AuthenticationPrincipal User userDetails) {
        log.info("request to create feedback");
        if(orgIdId.equals(null)){
            orgIdId = Long.valueOf(0);
        }
        try {
            feedBackService.addNewFeedBack(sessionId, coachId, orgIdId, feedback);
            return new ResponseEntity<>(new RestResponse(false,"FeedBack Received Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get feedback by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getFeedbackBySessionId(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "sessionId",required = false) Long sessionId
    ) {
        try {

            log.debug(
                    "REST request to get feedback given session Id {} and coachId {}",
                    sessionId
            );
            try {
                List<FeedbackDto> feedbackDto = feedBackService.getFeedbackBySessionId(
                        sessionId
                );
                return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error Occurred ", e);
                return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get feedback by client_id
    @GetMapping("/get-by-client-id")
    public ResponseEntity<?> getFeedbackByClientId(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "client_id",required = false) Long clientId
    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.debug(
                    "REST request to get feedback given client Id {} and coachId {}",
                    clientId,
                    coachId
            );
            FeedbackDto feedbackDto = feedBackService.getFeedbackByClientId(
                    clientId,
                    coachId
            );


            return ResponseEntity.ok().body(feedbackDto);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get ORg FeedBack
    @GetMapping(path = "getOrgFeedbacks/{id}")
    ResponseEntity<?> getOrgFeedbacks(@PathVariable("id") Long OrgId) {
        log.error("Request to get Organization Feedbacks");
        try{
            List<FeedbackDto> listResponse = feedBackService.getOrgFeedback(OrgId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET COACH FEEDBACKS
    @GetMapping(path = "getCoachFeedbacks/{id}")
    ResponseEntity<?> getCoachFeedbacks(@PathVariable("id") Long coachId) {
        log.error("Request to get COACH Feedbacks");
        try{
            List<FeedbackDto> listResponse = feedBackService.getCoachFeedback(coachId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
