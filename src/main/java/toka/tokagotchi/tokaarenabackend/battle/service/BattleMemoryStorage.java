package toka.tokagotchi.tokaarenabackend.battle.service;

import org.springframework.stereotype.Component;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BattleMemoryStorage {
    // Mapa que guarda: ID de Batalla -> Estado de la Batalla
    private final Map<String, BattleState> activeBattles = new ConcurrentHashMap<>();

    public void save(BattleState state) {
        activeBattles.put(state.getBattleId(), state);
    }

    public BattleState findById(String battleId) {
        return activeBattles.get(battleId);
    }

    public void remove(String battleId) {
        activeBattles.remove(battleId);
    }
}