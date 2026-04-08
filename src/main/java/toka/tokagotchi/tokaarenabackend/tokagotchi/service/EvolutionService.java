package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.EvolutionResultDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EvolutionService {

    private final TokagotchiRepository tokaRepo;
    private final UserRepository userRepo;
    private final TokagotchiMapper tokaMapper; // Asegúrate de inyectarlo
    private final Random random = new Random();

    @Transactional
    public EvolutionResultDTO evolve(Long tokaId) {
        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        User owner = toka.getOwner();

        // 1. Validar Cooldown (Sección 4 del PDF) [cite: 1]
        if (toka.getEvolutionCooldown() != null && toka.getEvolutionCooldown().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("El Tokagotchi aún está en cooldown de recuperación.");
        }

        Rarity currentRarity = toka.getRarity();
        EvolutionConfig config = getEvolutionConfig(currentRarity);

        // 2. Validar Requisitos (Sección 4 del PDF) [cite: 1]
        if (toka.getCp() < config.minCp) throw new RuntimeException("CP insuficientes: " + config.minCp);
        if (owner.getTf() < config.costTf) throw new RuntimeException("TF insuficiente: " + config.costTf);

        // 3. Cobrar TF [cite: 1]
        owner.setTf(owner.getTf() - config.costTf);
        userRepo.save(owner);

        // 4. Probabilidad de éxito [cite: 1]
        int roll = random.nextInt(100) + 1;
        if (roll <= config.successProb) {
            applyEvolution(toka, getNextRarity(currentRarity));
            toka.setEvolutionCooldown(null);
            tokaRepo.save(toka);
            return new EvolutionResultDTO(true, "¡Evolución exitosa!", tokaMapper.toResponse(toka));
        } else {
            // Fallo: Aplicar cooldown según tabla (12h, 24h, 48h) [cite: 1]
            toka.setEvolutionCooldown(LocalDateTime.now().plusHours(config.cooldownHours));
            tokaRepo.save(toka);
            return new EvolutionResultDTO(false, "El ascenso ha fallado.", tokaMapper.toResponse(toka));
        }
    }

    private void applyEvolution(Tokagotchi toka, Rarity nextRarity) {
        double oldMult = getMultiplier(toka.getRarity());
        double newMult = getMultiplier(nextRarity);

        // Recalcular stats manteniendo el jitter base [cite: 1]
        toka.setHp((int) Math.round((toka.getHp() / oldMult) * newMult));
        toka.setAtk((int) Math.round((toka.getAtk() / oldMult) * newMult));
        toka.setDef((int) Math.round((toka.getDef() / oldMult) * newMult));
        toka.setRarity(nextRarity);
    }

    private double getMultiplier(Rarity r) {
        return switch (r) {
            case COMMON -> 1.0;
            case RARE -> 1.15;
            case EPIC -> 1.35;
            case LEGENDARY -> 1.55;
        };
    }

    private EvolutionConfig getEvolutionConfig(Rarity current) {
        return switch (current) {
            case COMMON -> new EvolutionConfig(100, 10, 40, 12);
            case RARE -> new EvolutionConfig(300, 25, 30, 24);
            case EPIC -> new EvolutionConfig(600, 50, 20, 48);
            case LEGENDARY -> throw new RuntimeException("Ya es Legendario.");
        };
    }

    private Rarity getNextRarity(Rarity current) {
        return switch (current) {
            case COMMON -> Rarity.RARE;
            case RARE -> Rarity.EPIC;
            case EPIC -> Rarity.LEGENDARY;
            default -> null;
        };
    }

    private record EvolutionConfig(int minCp, int costTf, int successProb, int cooldownHours) {}
}