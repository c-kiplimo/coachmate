package ke.natujenge.baked.web.rest;

import ke.natujenge.baked.security.SecurityUtils;
import ke.natujenge.baked.service.BakerLocationService;
import ke.natujenge.baked.service.BakerSettingsService;
import ke.natujenge.baked.service.dto.BakerDTO;
import ke.natujenge.baked.service.dto.BakerSettingsDTO;
import ke.natujenge.baked.service.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api")
public class BakerSettingsResource {

    private final BakerSettingsService bakerSettingsService;

    public BakerSettingsResource(BakerSettingsService bakerSettingsService) {
        this.bakerSettingsService = bakerSettingsService;
    }

    @PostMapping("/baker-settings")
    public ResponseEntity<BakerSettingsDTO> saveSettings(@RequestBody BakerSettingsDTO bakerSettingsDTO) throws URISyntaxException {
        log.info("REST Request to save BakerSettings: {}", bakerSettingsDTO);
        if (bakerSettingsDTO.getBaker() == null){
            BakerDTO bakerDTO = new BakerDTO();
            bakerDTO.setId(SecurityUtils.getCurrentBakerId());
            bakerSettingsDTO.setBaker(bakerDTO);
        }
        BakerSettingsDTO result = bakerSettingsService.save(bakerSettingsDTO);


        return ResponseEntity.created(new URI("/api/baker-settings/"+result.getId())).body(result);
    }

    @GetMapping("/baker-settings/logo")
    public ResponseEntity<byte[]> getLogo() {
        log.info("REST Request to get Baker Logo");

        byte[] logoBytes = null;
        BakerSettingsDTO result = bakerSettingsService.findTopByBakerId(SecurityUtils.getCurrentBakerId());

        if (result != null && result.getLogo() != null){
            logoBytes = FileUtil.getImage(result.getLogo(), log);
        }

        return ResponseEntity.ok().body(logoBytes);
    }
}
