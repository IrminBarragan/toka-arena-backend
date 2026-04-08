package toka.tokagotchi.tokaarenabackend.tokagotchi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokagotchiMapper {

    private final AbilityRepository abilityRepository;

    public TokagotchiResponse toResponse(Tokagotchi toka) {

        return TokagotchiResponse.builder()
                .id(generateId(toka))
                .nombre(toka.getName())
                .especie(toka.getSpecies().name().toLowerCase())
                .rareza(mapRarity(toka.getRarity().name()))

                .stats(buildStats(toka))

                .habilidades(mapAbilities(toka))

                .accesorios(buildAccessories(toka))
                .assets(buildAssets(toka))

                .build();
    }

    // =========================
    // HELPERS
    // =========================

    private String generateId(Tokagotchi toka) {
        return toka.getSpecies().name().toLowerCase() + "_" + toka.getId();
    }

    private TokagotchiResponse.Stats buildStats(Tokagotchi toka) {
        return TokagotchiResponse.Stats.builder()
                .hp(toka.getHp())
                .atk(toka.getAtk())
                .def(toka.getDef())
                .nrg(100)
                .build();
    }

    private List<TokagotchiResponse.Habilidad> mapAbilities(Tokagotchi toka) {

        List<Ability> abilities = abilityRepository.findBySpecies(toka.getSpecies());

        System.out.println("Species: " + toka.getSpecies());
        System.out.println("Abilities found: " + abilities.size());
        
        return abilities.stream().map(a ->
                TokagotchiResponse.Habilidad.builder()
                        .id(a.getId())
                        .nombre(a.getNombre())
                        .costoNRG(a.getCostoNRG())
                        .multiplicador(a.getMultiplicador())
                        .descripcion(a.getDescripcion())
                        .esSignature(a.isEsSignature())
                        .build()
        ).toList();
    }

    private TokagotchiResponse.Accesorios buildAccessories(Tokagotchi toka) {
        return TokagotchiResponse.Accesorios.builder()
                .cabeza(null) // luego lo conectas con inventory
                .cuerpo(null)
                .build();
    }

    private TokagotchiResponse.Assets buildAssets(Tokagotchi toka) {
        String base = toka.getSpecies().name().toLowerCase();

        return TokagotchiResponse.Assets.builder()
                .armatureKey(base)
                .texPng("/assets/" + base + "/" + base + "_tex.png")
                .texJson("/assets/" + base + "/" + base + "_tex.json")
                .skeJson("/assets/" + base + "/" + base + "_ske.json")
                .build();
    }

    private String mapRarity(String rarity) {
        return switch (rarity) {
            case "COMMON" -> "Común";
            case "RARE" -> "Raro";
            case "EPIC" -> "Épico";
            case "LEGENDARY" -> "Legendario";
            default -> rarity;
        };
    }
}