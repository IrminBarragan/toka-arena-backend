package toka.tokagotchi.tokaarenabackend.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.battle.dto.AttackRequest;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleState;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final AbilityRepository abilityRepo;
    private final TokagotchiRepository tokaRepo;
    private final BattleMemoryStorage storage;

    public BattleState createBattle(Long myTokaId, Long opponentTokaId) {
        if (myTokaId.equals(opponentTokaId)) {
            throw new RuntimeException("No puedes iniciar batalla contra el mismo Tokagotchi");
        }

        Tokagotchi p1 = tokaRepo.findById(myTokaId).orElseThrow();
        Tokagotchi p2 = tokaRepo.findById(opponentTokaId).orElseThrow();

        if (p1.getOwner().getId().equals(p2.getOwner().getId())) {
            throw new RuntimeException("No puedes iniciar batalla contra un Tokagotchi del mismo dueño");
        }

        BattleState state = BattleState.builder()
                .battleId(UUID.randomUUID().toString())
                .player1Id(p1.getOwner().getId())
                .toka1Id(p1.getId())
                .toka1Hp(p1.getHp())
                .toka1Nrg(100)
                .player2Id(p2.getOwner().getId())
                .toka2Id(p2.getId())
                .toka2Hp(p2.getHp())
                .toka2Nrg(100)
                .currentTurnPlayerId(p1.getOwner().getId())
                .turnNumber(1)
                .finished(false)
                .build();

        storage.save(state);
        return state;
    }

    public BattleState getBattle(String battleId) {
        return storage.findById(battleId);
    }

    public BattleState performAttack(Long playerId, AttackRequest request) {
        BattleState state = storage.findById(request.getBattleId());
        if (state == null || state.isFinished()) throw new RuntimeException("Batalla no encontrada o finalizada");

        // 1. Validar turno
        if (!state.getCurrentTurnPlayerId().equals(playerId)) {
            throw new RuntimeException("No es tu turno");
        }

        // 2. Obtener datos del atacante y habilidad
        boolean isPlayer1 = state.getPlayer1Id().equals(playerId);
        Long attackerTokaId = isPlayer1 ? state.getToka1Id() : state.getToka2Id();

        Tokagotchi attacker = tokaRepo.findById(attackerTokaId).orElseThrow();
        Tokagotchi defender = tokaRepo.findById(isPlayer1 ? state.getToka2Id() : state.getToka1Id()).orElseThrow();
        Ability ability = abilityRepo.findById(request.getAbilityId()).orElseThrow();

        if (ability.getSpecies() != attacker.getSpecies()) {
            throw new RuntimeException("La habilidad no corresponde a la especie de tu Tokagotchi");
        }

        // 3. Validar NRG
        int currentNrg = isPlayer1 ? state.getToka1Nrg() : state.getToka2Nrg();
        if (currentNrg < ability.getEnergyCost()) {
            throw new RuntimeException("Energía insuficiente");
        }

        // 4. Calcular Daño
        int damage = (int) Math.max(1, (attacker.getAtk() * ability.getMultiplier()) - (defender.getDef() * 0.5));

        // 5. Aplicar cambios al estado
        if (isPlayer1) {
            state.setToka1Nrg(state.getToka1Nrg() - ability.getEnergyCost());
            state.setToka2Hp(Math.max(0, state.getToka2Hp() - damage));
            state.setCurrentTurnPlayerId(state.getPlayer2Id()); // Cambiar turno
        } else {
            state.setToka2Nrg(state.getToka2Nrg() - ability.getEnergyCost());
            state.setToka1Hp(Math.max(0, state.getToka1Hp() - damage));
            state.setCurrentTurnPlayerId(state.getPlayer1Id()); // Cambiar turno
        }

        // 6. Regenerar NRG al rival (+10 NRG al iniciar su turno)
        if (isPlayer1) state.setToka2Nrg(Math.min(100, state.getToka2Nrg() + 10));
        else state.setToka1Nrg(Math.min(100, state.getToka1Nrg() + 10));

        // 7. Verificar si alguien murió
        if (state.getToka1Hp() <= 0 || state.getToka2Hp() <= 0) {
            state.setFinished(true);
            state.setWinnerId(state.getToka1Hp() <= 0 ? state.getPlayer2Id() : state.getPlayer1Id());
        }

        state.setTurnNumber(state.getTurnNumber() + 1);
        storage.save(state);
        return state;
    }

    public List<Ability> getAbilitiesByToka(Long tokaId) {
        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        return abilityRepo.findBySpecies(toka.getSpecies());
    }
}