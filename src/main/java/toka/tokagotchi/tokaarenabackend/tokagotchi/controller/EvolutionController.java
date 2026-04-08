package toka.tokagotchi.tokaarenabackend.tokagotchi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.EvolutionResultDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.EvolutionService;

@RestController
@RequestMapping("/api/v1/tokagotchi")
@RequiredArgsConstructor
public class EvolutionController {

    private final EvolutionService evolutionService;

    @PostMapping("/{id}/evolve")
    public ResponseEntity<EvolutionResultDTO> evolve(@PathVariable Long id) {
        // Llamamos al servicio que ya tiene la lógica de DTOs para evitar recursión
        EvolutionResultDTO result = evolutionService.evolve(id);

        return ResponseEntity.ok(result);
    }
}