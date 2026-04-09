package toka.tokagotchi.tokaarenabackend.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.battle.dto.AttackRequest;
import toka.tokagotchi.tokaarenabackend.battle.dto.StartBattleRequest;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleMode;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleState;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
/**
 * Servicio principal del modulo de batalla.
 * Administra la creacion de combates, validaciones de turnos/habilidades
 * y la resolucion final incluyendo apuesta en modo riesgoso.
 */
public class BattleService {

    private final AbilityRepository abilityRepo;
    private final TokagotchiRepository tokaRepo;
    private final BattleMemoryStorage storage;

    /**
     * Inicia una batalla usando el usuario autenticado como retador.
     * Valida ownership del Tokagotchi atacante y permite seleccionar
     * modo NORMAL o RISKY con confirmacion explicita.
     */
    public BattleState createBattle(Long requesterId, StartBattleRequest request) {
        if (request == null || request.getMyTokaId() == null || request.getOpponentTokaId() == null) {
            throw new RuntimeException("Debes enviar los Tokagotchis para iniciar la batalla");
        }

        BattleMode mode = request.getMode() == null ? BattleMode.NORMAL : request.getMode();
        boolean riskConfirmed = request.isRiskConfirmed();

        Tokagotchi p1 = tokaRepo.findById(request.getMyTokaId()).orElseThrow();
        if (!p1.getOwner().getId().equals(requesterId)) {
            throw new RuntimeException("Solo puedes iniciar batallas con tus propios Tokagotchis");
        }

        Tokagotchi p2 = tokaRepo.findById(request.getOpponentTokaId()).orElseThrow();
        return createBattleInternal(p1, p2, mode, riskConfirmed);
    }

    public BattleState createBattle(Long myTokaId, Long opponentTokaId) {
        Tokagotchi p1 = tokaRepo.findById(myTokaId).orElseThrow();
        Tokagotchi p2 = tokaRepo.findById(opponentTokaId).orElseThrow();

        return createBattleInternal(p1, p2, BattleMode.NORMAL, false);
    }

    /**
     * Construye el estado inicial de batalla asegurando invariantes basicas:
     * no auto-combate, no mismo owner y confirmacion de riesgo en modo RISKY.
     */
    private BattleState createBattleInternal(Tokagotchi p1, Tokagotchi p2, BattleMode mode, boolean riskConfirmed) {
        if (p1.getId().equals(p2.getId())) {
            throw new RuntimeException("No puedes iniciar batalla contra el mismo Tokagotchi");
        }

        if (p1.getOwner().getId().equals(p2.getOwner().getId())) {
            throw new RuntimeException("No puedes iniciar batalla contra un Tokagotchi del mismo dueño");
        }

        if (mode == BattleMode.RISKY && !riskConfirmed) {
            throw new RuntimeException("Debes confirmar la batalla riesgosa antes de iniciar");
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
                .mode(mode.name())
                .riskConfirmed(riskConfirmed)
                .finished(false)
                .build();

        storage.save(state);
        return state;
    }

    public BattleState getBattle(String battleId) {
        return storage.findById(battleId);
    }

    /**
     * Ejecuta un turno de ataque:
     * valida turno y energia, aplica dano, cambia turno,
     * regenera energia rival y cierra la batalla si hay KO.
     */
    @Transactional
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
            applyRiskyStakeIfNeeded(state);
        }

        state.setTurnNumber(state.getTurnNumber() + 1);
        storage.save(state);
        return state;
    }

    /**
     * Si la batalla fue RISKY y estaba confirmada,
     * transfiere el Tokagotchi perdedor al owner del ganador.
     */
    private void applyRiskyStakeIfNeeded(BattleState state) {
        if (!"RISKY".equalsIgnoreCase(state.getMode()) || !state.isRiskConfirmed()) {
            return;
        }

        Long loserTokaId = state.getToka1Hp() <= 0 ? state.getToka1Id() : state.getToka2Id();
        Long winnerTokaId = state.getToka1Hp() <= 0 ? state.getToka2Id() : state.getToka1Id();

        Tokagotchi loserToka = tokaRepo.findById(loserTokaId).orElseThrow();
        Tokagotchi winnerToka = tokaRepo.findById(winnerTokaId).orElseThrow();

        if (!loserToka.getOwner().getId().equals(winnerToka.getOwner().getId())) {
            loserToka.setOwner(winnerToka.getOwner());
            tokaRepo.save(loserToka);
        }
    }

    public List<Ability> getAbilitiesByToka(Long tokaId) {
        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        return abilityRepo.findBySpecies(toka.getSpecies());
    }
}