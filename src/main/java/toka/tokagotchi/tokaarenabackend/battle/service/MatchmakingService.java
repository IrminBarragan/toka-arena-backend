package toka.tokagotchi.tokaarenabackend.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.battle.dto.MatchmakingOpponentDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;

import java.util.List;
import java.util.stream.Collectors;
/**
 * MatchmakingService: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final TokagotchiRepository tokaRepo;

    public List<Tokagotchi> findSuitableOpponents(Long myTokaId) {
        Tokagotchi myToka = tokaRepo.findById(myTokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        int myPL = myToka.calculatePowerLevel();

        // Definimos un margen de balanceo del 15%
        int range = (int) (myPL * 0.15);
        int minPL = myPL - range;
        int maxPL = myPL + range;

        // Buscamos en la base de datos Tokagotchis que no sean los nuestros
        // y que estén dentro del rango de Power Level
        return tokaRepo.findAll().stream()
                .filter(t -> !t.getOwner().getId().equals(myToka.getOwner().getId()))
                .filter(t -> {
                    int targetPL = t.calculatePowerLevel();
                    return targetPL >= minPL && targetPL <= maxPL;
                })
                .limit(5) // Devolvemos hasta 5 candidatos
                .collect(Collectors.toList());
    }

    public List<MatchmakingOpponentDTO> findOpponents(Long myTokaId) {
        Tokagotchi myToka = tokaRepo.findById(myTokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        int myPL = myToka.getPowerLevel();
        double margin = 0.15; // 15% de diferencia permitida

        return tokaRepo.findAll().stream()
                // No pelear contra uno mismo
                .filter(t -> !t.getId().equals(myTokaId))
                // No pelear contra otros Tokagotchis del mismo dueño
                .filter(t -> !t.getOwner().getId().equals(myToka.getOwner().getId()))
                // Filtrar por rango de Power Level
                .filter(t -> {
                    int opponentPL = t.getPowerLevel();
                    return opponentPL >= (myPL * (1 - margin)) &&
                            opponentPL <= (myPL * (1 + margin));
                })
                .limit(10) // Mostrar hasta 10 opciones
                .map(t -> MatchmakingOpponentDTO.builder()
                        .tokaId(t.getId())
                        .name(t.getName())
                        .species(t.getSpecies())
                        .rarity(t.getRarity())
                        .powerLevel(t.getPowerLevel())
                        .ownerName(t.getOwner().getUsername())
                        .build())
                .collect(Collectors.toList());
    }
}