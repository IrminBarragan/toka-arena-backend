package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.common.enums.AccessoryType;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserAccessoryRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.TokaBaseStats;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokagotchiService {

    private final TokagotchiRepository tokaRepo;
    private final UserRepository userRepo;
    private final UserAccessoryRepository userAccessoryRepo;
    private final TokagotchiMapper tokaMapper;
    private final AbilityRepository abilityRepository;
    private final Random random = new Random();

    @Transactional
    public TokagotchiResponse equipAccessory(Long tokaId, Long userAccessoryId) {
        User user = getAuthenticatedUser();

        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        if (!toka.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("No eres el dueño de este Tokagotchi");
        }

        UserAccessory userAcc = userAccessoryRepo.findById(userAccessoryId)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado en tu inventario"));

        if (!userAcc.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Este accesorio no te pertenece");
        }

        Accessory accessory = userAcc.getAccessory();
        Accessory currentlyEquipped = accessory.getType() == AccessoryType.HEAD
                ? toka.getEquippedHead()
                : toka.getEquippedBody();

        if (currentlyEquipped != null && !currentlyEquipped.getId().equals(accessory.getId())) {
            userAccessoryRepo.findByOwnerIdAndAccessoryId(user.getId(), currentlyEquipped.getId())
                    .ifPresent(previous -> {
                        previous.setEquipped(false);
                        userAccessoryRepo.save(previous);
                    });
        }

        // Lógica de slots según AccessoryType
        if (accessory.getType() == AccessoryType.HEAD) {
            toka.setEquippedHead(accessory);
        } else {
            toka.setEquippedBody(accessory);
        }

        userAcc.setEquipped(true);
        userAccessoryRepo.save(userAcc);

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public Tokagotchi awardExtraToka(User user, Rarity fixedRarity) {
        // Seleccionamos una especie al azar para la recompensa
        Species randomSpecies = Species.values()[random.nextInt(Species.values().length)];

        // Aplicamos stats base con jitter (Sección 1.1 y 1.2)
        TokaBaseStats base = TokaBaseStats.valueOf(randomSpecies.name());
        double mult = fixedRarity.getMultiplier();

        Tokagotchi extraToka = Tokagotchi.builder()
                .name(randomSpecies.name() + " de Regalo")
                .species(randomSpecies)
                .rarity(fixedRarity)
                .hp((int)(applyJitter(base.getHp()) * mult))
                .atk((int)(applyJitter(base.getAtk()) * mult))
                .def((int)(applyJitter(base.getDef()) * mult))
                .owner(user)
                .build();

        // Asignamos habilidades de la especie
        extraToka.setAbilities(abilityRepository.findBySpecies(randomSpecies));

        return tokaRepo.save(extraToka);
    }

    public Tokagotchi createStarter(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isFirstToka()) {
            throw new RuntimeException("Starter already started");
        }

        Rarity rarity = rollRarity();
        double rarityMultiplier = rarity.getMultiplier();

        Species species = Species.values()[random.nextInt(Species.values().length)];

        int baseHp = getBaseHp(species);
        int baseAtk = getBaseAtk(species);
        int baseDef = getBaseDef(species);

        int hp = (int) Math.round(applyJitter(baseHp) * rarityMultiplier);
        int atk = (int) Math.round(applyJitter(baseAtk) * rarityMultiplier);
        int def = (int) Math.round(applyJitter(baseDef) * rarityMultiplier);

        int cp = 0;
        List<Ability> speciesAbilities = abilityRepository.findBySpecies(species);

        Tokagotchi toka = Tokagotchi.builder()
                .name(species.name())
                .species(species)
                .rarity(rarity)
                .hp(hp)
                .atk(atk)
                .def(def)
                .cp(cp)
                .abilities(speciesAbilities)
                .owner(user)
                .build();

        user.setFirstToka(true);
        userRepo.save(user);

        return tokaRepo.save(toka);
    }

    public Tokagotchi renameTokagotchi(Long tokaId, String newName) {
        User user = getAuthenticatedUser();

        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow();

        if (!toka.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not your toka");
        }

        String normalizedName = newName == null ? null : newName.trim();
        if (normalizedName == null || normalizedName.length() < 3 || normalizedName.length() > 20) {
            throw new RuntimeException("Invalid name");
        }

        toka.setName(normalizedName);
        return tokaRepo.save(toka);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        final Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Token de usuario invalido");
        }

        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Tokagotchi getTokagotchiById(Long tokaId) {
        return tokaRepo.findById(tokaId)
                .orElseThrow();
    }

    private Rarity rollRarity() {
        int roll = random.nextInt(100) + 1;

        if (roll <= 80) return Rarity.COMMON;
        if (roll <= 95) return Rarity.RARE;
        return Rarity.EPIC;
    }

    private int applyJitter(int base) {
        double factor = 0.95 + (random.nextDouble() * 0.10); // 0.95 - 1.05
        return (int) Math.round(base * factor);
    }

    private int getBaseHp(Species s) {
        return switch (s) {
            case TOFU -> 100;
            case MOCHI -> 90;
            case HANA -> 125;
        };
    }

    private int getBaseAtk(Species s) {
        return switch (s) {
            case TOFU -> 12;
            case MOCHI -> 16;
            case HANA -> 10;
        };
    }

    private int getBaseDef(Species s) {
        return switch (s) {
            case TOFU -> 30;
            case MOCHI -> 20;
            case HANA -> 45;
        };
    }
}