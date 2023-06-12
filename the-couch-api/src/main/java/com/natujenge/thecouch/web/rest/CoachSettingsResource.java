package com.natujenge.thecouch.web.rest;


import com.natujenge.thecouch.security.SecurityUtils;
import com.natujenge.thecouch.service.dto.CoachSettingsDTO;
import com.natujenge.thecouch.service.dto.CoachDTO;
import com.natujenge.thecouch.util.FileUtil;
import com.natujenge.thecouch.service.CoachSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

//import static com.natujenge.thecouch.domain.QCoachSubscription.coachSubscription;

@Slf4j
@RestController
@RequestMapping("/api")
public class CoachSettingsResource {

    private final CoachSettingsService coachSettingsService;

    public CoachSettingsResource(CoachSettingsService coachSettingsService) {
        this.coachSettingsService = coachSettingsService;
    }

    @PostMapping("/coach-settings")
    public ResponseEntity<CoachSettingsDTO> saveSettings(@RequestBody CoachSettingsDTO coachSettingsDTO) throws URISyntaxException {
        log.info("REST Request to save CoachSettings: {}", coachSettingsDTO);
        if (coachSettingsDTO.getCoach() == null){
            CoachDTO coachDTO = new CoachDTO();
            coachDTO.setId(SecurityUtils.getCurrentCoachId());
            coachSettingsDTO.setCoach(coachDTO);
        }
            CoachSettingsDTO result = coachSettingsService.save(coachSettingsDTO);


        return ResponseEntity.created(new URI("/api/coach-settings/"+result.getId())).body(result);
    }

    @GetMapping("/coach-settings/logo")
    public ResponseEntity<byte[]> getLogo() {
        log.info("REST Request to get Coach Logo");

        byte[] logoBytes = null;
            CoachSettingsDTO result = coachSettingsService.findTopByCoachId(SecurityUtils.getCurrentCoachId());

        if (result != null && result.getLogo() != null){
            logoBytes = FileUtil.getImage(result.getLogo(), log);
        }

        return ResponseEntity.ok().body(logoBytes);
    }
}
