package toka.tokagotchi.tokaarenabackend.battle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.battle.dto.AttackRequest;
import toka.tokagotchi.tokaarenabackend.battle.dto.MatchmakingOpponentDTO;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleState;
import toka.tokagotchi.tokaarenabackend.battle.service.BattleService;
import toka.tokagotchi.tokaarenabackend.battle.service.MatchmakingService;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final MatchmakingService matchmakingService;

    @GetMapping("/matchmaking/{myTokaId}")
    public ResponseEntity<List<MatchmakingOpponentDTO>> getOpponents(@PathVariable Long myTokaId) {
        return ResponseEntity.ok(matchmakingService.findOpponents(myTokaId));
    }

    @PostMapping("/attack")
    public ResponseEntity<BattleState> attack(@AuthenticationPrincipal User user, @RequestBody AttackRequest request) {
        // Usamos el ID del usuario autenticado para validar que él sea quien ataca
        return ResponseEntity.ok(battleService.performAttack(user.getId(), request));
    }

    @PostMapping("/start/{myTokaId}/{opponentTokaId}")
    public ResponseEntity<BattleState> startBattle(@PathVariable Long myTokaId, @PathVariable Long opponentTokaId) {
        return ResponseEntity.ok(battleService.createBattle(myTokaId, opponentTokaId));
    }

    @GetMapping("/abilities/{tokaId}")
    public ResponseEntity<List<Ability>> getAbilities(@PathVariable Long tokaId) {
        return ResponseEntity.ok(battleService.getAbilitiesByToka(tokaId));
    }
}