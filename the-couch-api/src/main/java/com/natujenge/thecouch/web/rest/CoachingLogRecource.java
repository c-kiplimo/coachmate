package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.CoachingLog;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.CoachingLogService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.CoachingLogDTO;
import com.natujenge.thecouch.web.rest.request.CoachingLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/coaching-log")
public class CoachingLogRecource {

        private final CoachingLogService coachingLogService;

        public CoachingLogRecource(CoachingLogService coachingLogService) {
            this.coachingLogService = coachingLogService;
        }

        //CREATE
        @PostMapping
        public ResponseEntity<?> createCoachingLogs(
                @RequestBody List<CoachingLogRequest> coachingLogRequests,
                @AuthenticationPrincipal User userDetails
        ) {
            log.info("Request to create coaching logs");
            try {
                Long coachId = userDetails.getId();
                // Create a list to hold the created coaching logs
                List<CoachingLog> createdLogs = new ArrayList<>();

                // Iterate over the coachingLogRequests and create coaching logs
                for (CoachingLogRequest coachingLogRequest : coachingLogRequests) {
                    CoachingLog coachingLog = coachingLogService.createCoachingLog(coachingLogRequest, coachId);
                    createdLogs.add(coachingLog);
                }

                // Extract the IDs of the created coaching logs
                List<Long> createdLogIds = createdLogs.stream()
                        .map(CoachingLog::getId)
                        .collect(Collectors.toList());

                return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .build().toUri()
                ).body(createdLogIds);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        //get all coaching logs
        @GetMapping
        public ResponseEntity<List<CoachingLogDTO>> getAllCoachingLogs(
                @RequestParam(value = "search", required = false) String search,
                Pageable pageable,
                @AuthenticationPrincipal User userDetails
        ) {
            log.info("Request to get all coaching logs");
            Long coachId = userDetails.getId();
            Page<CoachingLogDTO> coachingLogDTOPage = coachingLogService.getAllCoachingLogs(search, pageable, coachId);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coachingLogDTOPage);
            return ResponseEntity.ok().headers(headers).body(coachingLogDTOPage.getContent());
        }

        //delete multiple coaching logs by id
        @DeleteMapping
        public ResponseEntity<?> deleteCoachingLogs(
                @RequestParam(value = "coachingLogIds" , required = false) List<Long> coachingLogIds,
                @AuthenticationPrincipal User userDetails
        ) {
            log.info("Request to delete coaching logs {}", coachingLogIds);
            Long coachId = userDetails.getId();
            try {
                coachingLogService.deleteCoachingLogs(coachingLogIds, coachId);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }


}
