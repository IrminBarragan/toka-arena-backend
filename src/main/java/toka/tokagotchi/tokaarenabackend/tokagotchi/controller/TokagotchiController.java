package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.RenameTokagotchiRequest;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.TokagotchiService;

@RestController
@RequestMapping("/api/v1/tokagotchi")
@RequiredArgsConstructor
public class TokagotchiController {

    private final TokagotchiService service;
    private final TokagotchiMapper mapper;

    @GetMapping("/{id}/get")
    public TokagotchiResponse getTokagotchiById(@PathVariable Long id) {
        return mapper.toResponse(service.getTokagotchiById(id));
    }

    @PostMapping("/claim-starter")
    public TokagotchiResponse claimStarter() {
        return mapper.toResponse(service.createStarter());
    }

    @PatchMapping("/{id}/rename")
    public TokagotchiResponse renameTokagotchi(@PathVariable long id, @RequestBody RenameTokagotchiRequest request) {
        return mapper.toResponse(service.reameTokagotchi(id, request.getNewName()));
    }
}