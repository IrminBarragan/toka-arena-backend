package toka.tokagotchi.tokaarenabackend.battle.dto;

import lombok.Data;
/**
 * AttackRequest: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class AttackRequest {
    private String battleId;
    private Long abilityId;
}