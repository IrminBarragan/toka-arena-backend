package toka.tokagotchi.tokaarenabackend.config.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.common.enums.AccessoryType;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.inventory.repository.AccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.ConsumableRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserAccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserConsumableRepository;
import toka.tokagotchi.tokaarenabackend.missions.model.Mission;
import toka.tokagotchi.tokaarenabackend.missions.repository.MissionRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
import toka.tokagotchi.tokaarenabackend.tokagotchi.repository.TokagotchiRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AccessoryRepository accessoryRepository;
    private final ConsumableRepository consumableRepository;
    private final UserAccessoryRepository userAccessoryRepository;
    private final UserConsumableRepository userConsumableRepository;
    private final AbilityRepository abilityRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final TokagotchiRepository tokagotchiRepository;

    @Override
    public void run(String... args) throws Exception {
        loadAccessories();
        loadConsumables();
        loadUserData();
        loadAbilities();
        loadMissions();
        loadTestOpponents();
        System.out.println(" Datos iniciales cargados correctamente.");
    }

    private void loadAccessories() {
        if (accessoryRepository.count() > 0) return;

        // Basado en la sección 10.2: Costos de Accesorios (página 4)
        accessoryRepository.save(Accessory.builder()
                .name("Sombrero")
                .type(AccessoryType.HEAD)
                .price(8.0) // Costo: 8 TF [cite: 173]
                .build());

        accessoryRepository.save(Accessory.builder()
                .name("Corona")
                .type(AccessoryType.HEAD)
                .price(10.0) // Costo: 10 TF [cite: 174]
                .build());

        accessoryRepository.save(Accessory.builder()
                .name("Casco")
                .type(AccessoryType.HEAD)
                .price(8.0) // Costo: 8 TF [cite: 175]
                .build());

        accessoryRepository.save(Accessory.builder()
                .name("Super Capa")
                .type(AccessoryType.BODY)
                .price(10.0) // Costo: 10 TF [cite: 176]
                .build());
    }

    private void loadConsumables() {
        if (consumableRepository.count() > 0) return;

        // Basado en la sección 9: Consumibles (Pociones) (página 4)
        consumableRepository.save(Consumable.builder()
                .name("Poción Pequeña")
                .description("+20 HP") // Efecto: +20 HP
                .price(5.0)           // Costo: 5 TF [cite: 167]
                .build());

        consumableRepository.save(Consumable.builder()
                .name("Poción Mediana")
                .description("+50 HP") // Efecto: +50 HP
                .price(10.0)          // Costo: 10 TF [cite: 168]
                .build());

        consumableRepository.save(Consumable.builder()
                .name("Poción Grande")
                .description("Full HP") // Efecto: Full HP
                .price(15.0)           // Costo: 15 TF [cite: 169]
                .build());

        consumableRepository.save(Consumable.builder()
                .name("Escudo")
                .description("Escudo 25% HP (2 turnos)") // Efecto: Escudo 25% HP
                .price(8.0)                             // Costo: 8 TF [cite: 171]
                .build());
    }

    private void loadUserData() {
        // 1. Verificar si el usuario con ID 1 existe (creado por el modo DEBUG o manual)
        User user = userRepository.findById(1L).orElse(null);
        if (user == null || userAccessoryRepository.count() > 0) return;

        // 2. Llenar Accesorios para el Usuario 1 (Sección 10.2 del documento)
        // Buscamos los accesorios base cargados previamente
        Accessory casco = accessoryRepository.findByName("Casco").orElseThrow();
        Accessory capa = accessoryRepository.findByName("Super Capa").orElseThrow();

        userAccessoryRepository.save(UserAccessory.builder()
                .owner(user)
                .accessory(casco)
                .equipped(false)
                .build());

        userAccessoryRepository.save(UserAccessory.builder()
                .owner(user)
                .accessory(capa)
                .equipped(false)
                .build());

        // 3. Llenar Consumibles para el Usuario 1 (Sección 9 del documento)
        Consumable pocionMediana = consumableRepository.findByName("Poción Mediana").orElseThrow();
        Consumable escudo = consumableRepository.findByName("Escudo").orElseThrow();

        userConsumableRepository.save(UserConsumable.builder()
                .owner(user)
                .consumable(pocionMediana)
                .quantity(3) // Le damos 3 pociones para probar
                .build());

        userConsumableRepository.save(UserConsumable.builder()
                .owner(user)
                .consumable(escudo)
                .quantity(1) // Le damos 1 escudo (Costo 8 TF según doc)
                .build());
    }

    private void loadAbilities() {
        if (abilityRepository.count() > 0) return;

        // TOFU (PERRO)
        abilityRepository.saveAll(List.of(
                Ability.builder().name("Mordida").energyCost(15).multiplier(1.0).species(Species.TOFU)
                        .description("Daño 1.0x Atk").isSignature(false).scaleWithRarity(true).build(),
                Ability.builder().name("Ladrido").energyCost(20).multiplier(0.0).species(Species.TOFU)
                        .description("+15% Ataque por 2 turnos").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Guardia").energyCost(25).multiplier(0.0).species(Species.TOFU)
                        .description("-30% daño recibido el próximo turno").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Lealtad").energyCost(45).multiplier(1.4).species(Species.TOFU)
                        .description("Signature: Daño 1.4x Atk. Si HP < 30%, cura 20% del daño").isSignature(true).scaleWithRarity(true).build()
        ));

        // MOCHI (GATO)
        abilityRepository.saveAll(List.of(
                Ability.builder().name("Zarpazo").energyCost(15).multiplier(0.9).species(Species.MOCHI)
                        .description("Daño 0.9x Atk. 20% prob. de ignorar defensa").isSignature(false).scaleWithRarity(true).build(),
                Ability.builder().name("Agilidad").energyCost(25).multiplier(0.0).species(Species.MOCHI)
                        .description("25% prob. de esquivar el siguiente ataque").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Bufido").energyCost(20).multiplier(0.0).species(Species.MOCHI)
                        .description("Reduce Defensa del rival un 20%").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Frenesí").energyCost(45).multiplier(1.4).species(Species.MOCHI)
                        .description("Signature: 2 golpes de 0.7x Atk. Crítico en Legendario").isSignature(true).scaleWithRarity(true).build()
        ));

        // HANA
        abilityRepository.saveAll(List.of(
                Ability.builder().name("Embestida").energyCost(15).multiplier(0.4).species(Species.HANA)
                        .description("Daño basado en Defensa (Def * 0.4)").isSignature(false).scaleWithRarity(true).build(),
                Ability.builder().name("Piel de Roble").energyCost(25).multiplier(0.0).species(Species.HANA)
                        .description("Escudo de absorción (20% HP Max)").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Regeneración").energyCost(30).multiplier(0.0).species(Species.HANA)
                        .description("Cura 5% HP/turno por 3 turnos").isSignature(false).scaleWithRarity(false).build(),
                Ability.builder().name("Bosque Ancestral").energyCost(45).multiplier(0.0).species(Species.HANA)
                        .description("Signature: Refleja 40% daño (60% en Legendario)").isSignature(true).scaleWithRarity(true).build()
        ));
    }

    private void loadMissions() {
        if (missionRepository.count() > 0) return;

        missionRepository.saveAll(List.of(
                Mission.builder().description("ALIMENTA A TU TOKA").requiredAmount(1).rewardTf(2.0).type("ALIMENTAR").build(),
                Mission.builder().description("JUEGA CON TU TOKA").requiredAmount(1).rewardTf(3.0).type("JUGAR").build(),
                Mission.builder().description("BAÑA A TU TOKA").requiredAmount(1).rewardTf(2.0).type("BAÑAR").build()
        ));
    }

    private void loadTestOpponents() {
        if (userRepository.count() > 1) return; // Solo cargar si solo existe el usuario DEBUG

        // 1. Crear Rivales (Sección 10.1: Todos inician con algo de TF)
        User zafiro = userRepository.save(User.builder()
                .username("Rival_Zafiro").talentLandUserId("EXT_001").tf(150.0).firstToka(true).build());
        User mochiMaster = userRepository.save(User.builder()
                .username("Rival_Mochi_Master").talentLandUserId("EXT_002").tf(50.0).firstToka(true).build());
        User hanaTank = userRepository.save(User.builder()
                .username("Rival_Hana_Tank").talentLandUserId("EXT_003").tf(200.0).firstToka(true).build());

        // 2. Crear Tokagotchis para los rivales con Multiplicadores de Rareza (Sección 1.2)

        // Tofu Raro (x1.15)
        tokagotchiRepository.save(Tokagotchi.builder()
                .name("Tofu Guardián").species(Species.TOFU).rarity(Rarity.RARE)
                .hp((int)(100 * 1.15)).atk((int)(13 * 1.15)).def((int)(30 * 1.15))
                .owner(zafiro).abilities(abilityRepository.findBySpecies(Species.TOFU))
                .build());

        // Mochi Épico (x1.35) - El "Glass Cannon" (Sección 1.1)
        tokagotchiRepository.save(Tokagotchi.builder()
                .name("Mochi Veloz").species(Species.MOCHI).rarity(Rarity.EPIC)
                .hp((int)(90 * 1.35)).atk((int)(16 * 1.35)).def((int)(20 * 1.35))
                .owner(mochiMaster).abilities(abilityRepository.findBySpecies(Species.MOCHI))
                .build());

        // Hana Común (x1.00) - El Tanque (Sección 1.1)
        tokagotchiRepository.save(Tokagotchi.builder()
                .name("Hana Bosque").species(Species.HANA).rarity(Rarity.COMMON)
                .hp(125).atk(10).def(45)
                .owner(hanaTank).abilities(abilityRepository.findBySpecies(Species.HANA))
                .build());
    }
}