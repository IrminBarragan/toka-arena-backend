package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication required");
        }

        String userIdStr = authentication.getName();
        Long userId = Long.parseLong(userIdStr);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow(() -> new RuntimeException("Tokagotchi no encontrado"));

        if (!toka.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("No eres el dueño de este Tokagotchi");
        }

        UserAccessory userAcc = userAccessoryRepo.findByAccessory_Id(userAccessoryId)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado en tu inventario"));

        if (!userAcc.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Este accesorio no te pertenece");
        }

        Accessory accessory = userAcc.getAccessory();

        // Lógica de slots según AccessoryType
        if (accessory.getType().name().equals("HEAD")) {
            toka.setEquippedHead(accessory);
        } else {
            toka.setEquippedBody(accessory);
        }

        return tokaMapper.toResponse(tokaRepo.save(toka));
    }

    @Transactional
    public Tokagotchi awardExtraToka(User user, Rarity fixedRarity) {
        // Seleccionamos una especie al azar para la recompensa
        Species randomSpecies = Species.values()[new Random().nextInt(Species.values().length)];

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

    @Transactional
    public TokagotchiResponse createStarter(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isFirstToka()) {
            throw new RuntimeException("Starter already started");
        }

        Rarity rarity = rollRarity();

        Species species = Species.values()[random.nextInt(Species.values().length)];

        int baseHp = getBaseHp(species);
        int baseAtk = getBaseAtk(species);
        int baseDef = getBaseDef(species);

        int hp = applyJitter(baseHp);
        int atk = applyJitter(baseAtk);
        int def = applyJitter(baseDef);

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
        toka = tokaRepo.save(toka);
        List<Tokagotchi> tokagotchis = user.getTokagotchis();
        tokagotchis.add(toka);

        user.setTokagotchis(tokagotchis);
        user.setTokagotchiActivo(toka);
        userRepo.save(user);
        return tokaMapper.toResponse(toka);
    }

    public Tokagotchi reameTokagotchi(Long tokaId, String newName) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication required");
        }

        String username = authentication.getName();
        Long userId = Long.parseLong(username);

        User user = userRepo.findById(userId)
                .orElseThrow();

        Tokagotchi toka = tokaRepo.findById(tokaId)
                .orElseThrow();

        if (!toka.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not your toka");
        }

        if (newName == null || newName.length() < 3 || newName.length() > 20) {
            throw new RuntimeException("Invalid name");
        }

        toka.setName(newName);
        return tokaRepo.save(toka);
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