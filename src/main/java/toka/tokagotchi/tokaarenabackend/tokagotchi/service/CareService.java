package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toka.tokagotchi.tokaarenabackend.missions.Service.MissionService;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;

import java.time.LocalDateTime;
/**
 * CareService: componente del modulo `tokagotchi`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Service
@RequiredArgsConstructor
public class CareService {

    private final TokagotchiRepository tokaRepo;
    private final TokagotchiMapper tokaMapper;
    private final MissionService missionService;

    @Transactional
    public TokagotchiResponse feed(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 10m
        validateCooldown(toka.getLastFed(), 10, "alimentar");

        toka.setCp(toka.getCp() + 5);
        toka.setLastFed(LocalDateTime.now());
        missionService.updateProgress(toka.getOwner().getId(), "ALIMENTAR");

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public TokagotchiResponse play(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 20m
        validateCooldown(toka.getLastPlayed(), 20, "jugar");

        toka.setCp(toka.getCp() + 8);
        toka.setLastPlayed(LocalDateTime.now());
        missionService.updateProgress(toka.getOwner().getId(), "JUGAR");

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public TokagotchiResponse bathe(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 30m
        validateCooldown(toka.getLastBathed(), 30, "bañar");

        toka.setCp(toka.getCp() + 4);
        toka.setLastBathed(LocalDateTime.now());
        missionService.updateProgress(toka.getOwner().getId(), "BAÑAR");
        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    private Tokagotchi findAndValidate(Long id) {
        Tokagotchi toka = tokaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        Long authenticatedUserId = getAuthenticatedUserId();
        if (!toka.getOwner().getId().equals(authenticatedUserId)) {
            throw new RuntimeException("No eres el dueño de este Tokagotchi");
        }

        return toka;
    }

    private void validateCooldown(LocalDateTime lastAction, int minutes, String actionName) {
        if (lastAction != null && lastAction.plusMinutes(minutes).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Debes esperar antes de volver a " + actionName);
        }
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Token de usuario invalido");
        }
    }
}