package toka.tokagotchi.tokaarenabackend.battle.dto;

import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;

@Data
@Builder
public class MatchmakingOpponentDTO {
    private Long tokaId;
    private String name;
    private Species species;
    private Rarity rarity;
    private int powerLevel;
    private String ownerName;
}
