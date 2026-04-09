package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.RenameTokagotchiRequest;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.TokagotchiService;

@RestController
@RequestMapping("/api/v1/tokagotchi")
@RequiredArgsConstructor
public class TokagotchiController {

    private final TokagotchiService service;
    private final TokagotchiMapper mapper;

    @GetMapping("/{id}/get")
    public ResponseEntity<TokagotchiResponse> getTokagotchiById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getTokagotchiById(id)));
    }

    @PostMapping("/claim-starter")
    public ResponseEntity<?> claimStarter() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = Long.parseLong(userIdStr);

        Tokagotchi response = service.createStarter(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/rename")
    public TokagotchiResponse renameTokagotchi(@PathVariable long id, @RequestBody RenameTokagotchiRequest request) {
        return mapper.toResponse(service.reameTokagotchi(id, request.getNewName()));
    }

    @PostMapping("/{tokaId}/equip/{userAccessoryId}")
    public ResponseEntity<TokagotchiResponse> equip(
            @PathVariable Long tokaId,
            @PathVariable Long userAccessoryId) {
        return ResponseEntity.ok(service.equipAccessory(tokaId, userAccessoryId));
    }
}