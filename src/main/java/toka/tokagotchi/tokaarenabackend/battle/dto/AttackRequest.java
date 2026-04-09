package toka.tokagotchi.tokaarenabackend.battle.dto;

import lombok.Data;

@Data
public class AttackRequest {
    private String battleId;
    private Long abilityId;
}