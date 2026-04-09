package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CareService {

    private final TokagotchiRepository tokaRepo;
    private final TokagotchiMapper tokaMapper;

    @Transactional
    public TokagotchiResponse feed(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 10m
        validateCooldown(toka.getLastFed(), 10, "alimentar");

        toka.setCp(toka.getCp() + 5);
        toka.setLastFed(LocalDateTime.now());

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public TokagotchiResponse play(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 20m
        validateCooldown(toka.getLastPlayed(), 20, "jugar");

        toka.setCp(toka.getCp() + 8);
        toka.setLastPlayed(LocalDateTime.now());

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public TokagotchiResponse bathe(Long tokaId) {
        Tokagotchi toka = findAndValidate(tokaId);

        // Cooldown 30m
        validateCooldown(toka.getLastBathed(), 30, "bañar");

        toka.setCp(toka.getCp() + 4);
        toka.setLastBathed(LocalDateTime.now());

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    private Tokagotchi findAndValidate(Long id) {
        return tokaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));
    }

    private void validateCooldown(LocalDateTime lastAction, int minutes, String actionName) {
        if (lastAction != null && lastAction.plusMinutes(minutes).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Debes esperar antes de volver a " + actionName);
        }
    }
}