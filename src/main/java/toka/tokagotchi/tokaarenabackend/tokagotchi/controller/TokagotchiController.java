package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.TokagotchiService;

@RestController
@RequestMapping("/api/v1/tokagotchi")
@RequiredArgsConstructor
public class TokagotchiController {

    private final TokagotchiService service;
    private final TokagotchiMapper mapper;

    @PostMapping("/claim-starter")
    public TokagotchiResponse claimStarter() {
        return mapper.toResponse(service.createStarter());
    }
}