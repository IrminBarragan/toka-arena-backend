package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;

import java.util.List;

@Data
@Builder
public class TokagotchiResponse {
    private Long id;
    private String name;
    private Species species;
    private Rarity rarity;
    private int hp;
    private int atk;
    private int def;
    private int cp;

    private Accessory equippedHead;
    private Accessory equippedBody;

    private List<AbilityDTO> abilities;
}