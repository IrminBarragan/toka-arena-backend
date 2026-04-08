package toka.tokagotchi.tokaarenabackend.tokagotchi.mapper;

import org.springframework.stereotype.Component;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

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
                .equippedBody(toka.getEquippedBody())
                .build();
    }
}