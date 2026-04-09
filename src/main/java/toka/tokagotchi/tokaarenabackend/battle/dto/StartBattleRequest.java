package toka.tokagotchi.tokaarenabackend.battle.dto;

import lombok.Data;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleMode;
/**
 * StartBattleRequest: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class StartBattleRequest {
    private Long myTokaId;
    private Long opponentTokaId;
    private BattleMode mode;
    private boolean riskConfirmed;
}

