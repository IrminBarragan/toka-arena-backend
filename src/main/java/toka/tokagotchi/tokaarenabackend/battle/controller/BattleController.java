package toka.tokagotchi.tokaarenabackend.battle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.battle.dto.AttackRequest;
import toka.tokagotchi.tokaarenabackend.battle.dto.MatchmakingOpponentDTO;
import toka.tokagotchi.tokaarenabackend.battle.dto.StartBattleRequest;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.model.BattleState;
import toka.tokagotchi.tokaarenabackend.battle.service.BattleService;
import toka.tokagotchi.tokaarenabackend.battle.service.MatchmakingService;

import java.util.List;
/**
 * BattleController: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


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
    public ResponseEntity<BattleState> attack(@AuthenticationPrincipal Object principal, @RequestBody AttackRequest request) {
        Long userId = resolveUserId(principal);
        return ResponseEntity.ok(battleService.performAttack(userId, request));
    }

    private Long resolveUserId(Object principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        if (principal instanceof String principalValue) {
            try {
                return Long.parseLong(principalValue);
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Principal JWT invalido");
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tipo de principal no soportado");
    }

    @PostMapping("/start/{myTokaId}/{opponentTokaId}")
    public ResponseEntity<BattleState> startBattle(
            @AuthenticationPrincipal Object principal,
            @PathVariable Long myTokaId,
            @PathVariable Long opponentTokaId
    ) {
        Long userId = resolveUserId(principal);
        StartBattleRequest request = new StartBattleRequest();
        request.setMyTokaId(myTokaId);
        request.setOpponentTokaId(opponentTokaId);
        return ResponseEntity.ok(battleService.createBattle(userId, request));
    }

    @PostMapping("/start")
    public ResponseEntity<BattleState> startBattleWithMode(
            @AuthenticationPrincipal Object principal,
            @RequestBody StartBattleRequest request
    ) {
        Long userId = resolveUserId(principal);
        return ResponseEntity.ok(battleService.createBattle(userId, request));
    }

    @GetMapping("/abilities/{tokaId}")
    public ResponseEntity<List<Ability>> getAbilities(@PathVariable Long tokaId) {
        return ResponseEntity.ok(battleService.getAbilitiesByToka(tokaId));
    }
}