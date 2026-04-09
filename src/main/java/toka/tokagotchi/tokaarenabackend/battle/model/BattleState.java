package toka.tokagotchi.tokaarenabackend.battle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattleState implements Serializable {
    private String battleId;

    // Jugador Atacante (Retador)
    private Long player1Id;
    private Long toka1Id;
    private int toka1Hp;
    private int toka1Nrg; // Inicia en 100

    // Jugador Defensor (Rival)
    private Long player2Id;
    private Long toka2Id;
    private int toka2Hp;
    private int toka2Nrg; // Inicia en 100

    private Long currentTurnPlayerId;
    private int turnNumber;
    private String mode;
    private boolean riskConfirmed;
    private boolean finished;
    private Long winnerId;
}