package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.EvolutionResultDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.EvolutionService;

import java.util.Map;
/**
 * EvolutionController: componente del modulo `tokagotchi`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@RestController
@RequestMapping("/api/v1/tokagotchi")
@RequiredArgsConstructor
public class EvolutionController {

    private final EvolutionService evolutionService;

    @PostMapping("/evolve/{id}")
    public ResponseEntity<?> evolve(@PathVariable Long id) {
        try {
            EvolutionResultDTO result = evolutionService.evolve(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage(),
                            "error", e.getMessage()
                    ));
        }
    }
}