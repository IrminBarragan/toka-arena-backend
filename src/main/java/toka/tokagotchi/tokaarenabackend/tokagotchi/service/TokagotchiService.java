package toka.tokagotchi.tokaarenabackend.tokagotchi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokagotchiService {

    private final TokagotchiRepository tokaRepo;
    private final UserRepository userRepo;

    private final Random random = new Random();

    public Tokagotchi createStarter() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByUsername(username)
                .orElseThrow();

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

        int happiness = 80;
        int cp = 0;

        Tokagotchi toka = Tokagotchi.builder()
                .name(species.name())
                .species(species)
                .rarity(rarity)
                .hp(hp)
                .atk(atk)
                .def(def)
                .happiness(happiness)
                .cp(cp)
                .owner(user)
                .build();

        user.setFirstToka(true);
        userRepo.save(user);

        return tokaRepo.save(toka);
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