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
                                        @RequestParam(name = "session_id",required = false) Long sessionId,
                                        @AuthenticationPrincipal User userDetails) {
        log.info("request to create feedback");
        try {
            feedBackService.addNewFeedBack(sessionId,userDetails.getCoach().getId(), feedback);
            return new ResponseEntity<>(new RestResponse(false,"FeedBack Received Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get feedback by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getFeedbackBySessionId(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "session_id",required = false) Long sessionId
    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.debug(
                    "REST request to get feedback given session Id {} and coachId {}",
                    sessionId,
                    coachId
            );
            FeedbackDto feedbackDto = feedBackService.getFeedbackBySessionId(
                    sessionId,
                    coachId
            );


            return ResponseEntity.ok().body(feedbackDto);
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
}
