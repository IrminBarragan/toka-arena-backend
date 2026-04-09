package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.CareService;
/**
 * CareController: componente del modulo `tokagotchi`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@RestController
@RequestMapping("/api/v1/tokagotchi/care")
@RequiredArgsConstructor
public class CareController {

    private final CareService careService;

    @PostMapping("/{id}/feed")
    public ResponseEntity<TokagotchiResponse> feed(@PathVariable Long id) {
        return ResponseEntity.ok(careService.feed(id));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<TokagotchiResponse> play(@PathVariable Long id) {
        return ResponseEntity.ok(careService.play(id));
    }

    @PostMapping("/{id}/bathe")
    public ResponseEntity<TokagotchiResponse> bathe(@PathVariable Long id) {
        return ResponseEntity.ok(careService.bathe(id));
    }
}