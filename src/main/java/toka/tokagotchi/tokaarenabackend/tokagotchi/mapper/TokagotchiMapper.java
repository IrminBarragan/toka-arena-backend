package toka.tokagotchi.tokaarenabackend.tokagotchi.mapper;

import org.springframework.stereotype.Component;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.AbilityDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

import static java.util.stream.Collectors.toList;

@Component
public class TokagotchiMapper {

    public TokagotchiResponse toResponse(Tokagotchi toka) {
        if (toka == null) return null;

        return TokagotchiResponse.builder()
                .id(toka.getId())
                .name(toka.getName())
                .species(toka.getSpecies())
                .rarity(toka.getRarity())
                .hp(toka.getHp())
                .atk(toka.getAtk())
                .def(toka.getDef())
                .cp(toka.getCp())
                .equippedHead(toka.getEquippedHead())
                .abilities(toka.getAbilities().stream()
                        .map(ability -> AbilityDTO.builder()
                                .id(ability.getId())
                                .name(ability.getName())
                                .energyCost(ability.getEnergyCost())
                                .multiplier(ability.getMultiplier())
                                .description(ability.getDescription())
                                .build())
                        .collect(toList()))
                .equippedBody(toka.getEquippedBody())
                .build();
    }
}