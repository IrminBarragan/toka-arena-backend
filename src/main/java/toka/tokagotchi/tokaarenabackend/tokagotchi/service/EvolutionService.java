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
    private final TokagotchiMapper tokaMapper;
    private final Random random = new Random();

    @Transactional
    public EvolutionResultDTO evolve(Long tokaId) {
        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        User owner = toka.getOwner();

        // 1. Validar Cooldown (Sección 4 del PDF)
        // Mensaje específico solicitado para errores de tiempo
        if (toka.getEvolutionCooldown() != null && toka.getEvolutionCooldown().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Espera para regresar");
        }

        Rarity currentRarity = toka.getRarity();
        EvolutionConfig config = getEvolutionConfig(currentRarity);

        // 2. Validar Requisitos específicos (Sección 4 del PDF)
        if (toka.getCp() < config.minCp) {
            throw new RuntimeException("CP insuficientes. Necesitas " + config.minCp + " puntos de cuidado.");
        }
        if (owner.getTf() < config.costTf) {
            throw new RuntimeException("No tienes suficientes Toka Feed (TF). Costo: " + config.costTf);
        }

        // 3. Cobrar TF [cite: 18, 70, 71, 73]
        owner.setTf(owner.getTf() - config.costTf);
        userRepo.save(owner);

        // 4. Probabilidad de Éxito
        int roll = random.nextInt(100) + 1;
        if (roll <= config.successProb) {
            applyEvolution(toka, getNextRarity(currentRarity));
            toka.setEvolutionCooldown(null);
            tokaRepo.save(toka);
            // Mensaje específico solicitado para éxito
            return new EvolutionResultDTO(true, "Cuidado correcto: ¡Ascenso exitoso!", tokaMapper.toResponse(toka));
        } else {
            // Fallo: Aplicar cooldown según tabla (12h, 24h, 48h)
            toka.setEvolutionCooldown(LocalDateTime.now().plusHours(config.cooldownHours));
            tokaRepo.save(toka);
            return new EvolutionResultDTO(false, "El ascenso ha fallado. Inténtalo más tarde.", tokaMapper.toResponse(toka));
        }
    }

    private void applyEvolution(Tokagotchi toka, Rarity nextRarity) {
        // Multiplicadores según Sección 1.2
        double oldMult = toka.getRarity().getMultiplier();
        double newMult = nextRarity.getMultiplier();

        // Recalcular stats manteniendo el jitter base [cite: 13]
        toka.setHp((int) Math.round((toka.getHp() / oldMult) * newMult));
        toka.setAtk((int) Math.round((toka.getAtk() / oldMult) * newMult));
        toka.setDef((int) Math.round((toka.getDef() / oldMult) * newMult));
        toka.setRarity(nextRarity);
    }

    private EvolutionConfig getEvolutionConfig(Rarity current) {
        // Datos basados en la tabla de la Sección 4
        return switch (current) {
            case COMMON -> new EvolutionConfig(100, 10, 40, 12);
            case RARE -> new EvolutionConfig(300, 25, 30, 24);
            case EPIC -> new EvolutionConfig(600, 50, 20, 48);
            case LEGENDARY -> throw new RuntimeException("Tu Tokagotchi ya alcanzó el nivel máximo (Legendario).");
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