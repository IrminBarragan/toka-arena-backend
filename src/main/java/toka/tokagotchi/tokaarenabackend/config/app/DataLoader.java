package toka.tokagotchi.tokaarenabackend.config.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.common.enums.AccessoryType;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.repository.AccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.ConsumableRepository;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AbilityRepository abilityRepository;
    private final AccessoryRepository accessoryRepository;
    private final ConsumableRepository consumableRepository;

    @Override
    public void run(String... args) {
        loadAbilities();
        loadAccessories();
        loadConsumables();
    }

    private void loadAbilities() {
        if (abilityRepository.count() > 0) return;

        abilityRepository.save(Ability.builder()
                .id("zarpazo")
                .nombre("Zarpazo")
                .costoNRG(15)
                .multiplicador(0.9)
                .descripcion("Daño 0.9x Atk. 20% prob. de ignorar defensa")
                .esSignature(false)
                .species(Species.MOCHI)
                .build());

        abilityRepository.save(Ability.builder()
                .id("agilidad")
                .nombre("Agilidad")
                .costoNRG(25)
                .descripcion("25% prob. de esquivar el siguiente ataque")
                .esSignature(false)
                .species(Species.MOCHI)
                .build());

        abilityRepository.save(Ability.builder()
                .id("bufido")
                .nombre("Bufido")
                .costoNRG(20)
                .descripcion("Reduce Defensa del rival un 20%")
                .esSignature(false)
                .species(Species.MOCHI)
                .build());

        abilityRepository.save(Ability.builder()
                .id("frenesi")
                .nombre("Frenesí")
                .costoNRG(45)
                .multiplicador(0.7)
                .descripcion("2 golpes de 0.7x Atk. En Legendario, 30% prob. de crítico (x1.5)")
                .esSignature(true)
                .species(Species.MOCHI)
                .build());
    }

    private void loadAccessories() {
        if (accessoryRepository.count() == 0) {
            accessoryRepository.save(Accessory.builder().name("Sombrero").price(8).type(AccessoryType.HEAD).build());
            accessoryRepository.save(Accessory.builder().name("Casco").price(8).type(AccessoryType.HEAD).build());
            accessoryRepository.save(Accessory.builder().name("Corona").price(10).type(AccessoryType.HEAD).build());
            accessoryRepository.save(Accessory.builder().name("Super capa").price(10).type(AccessoryType.BODY).build());
            System.out.println("Accesorios cargados.");
        }
    }

    private void loadConsumables() {
        if (consumableRepository.count() == 0) {
            consumableRepository.save(Consumable.builder().name("Poción pequeña").price(5).description("+20 HP").build());
            consumableRepository.save(Consumable.builder().name("Poción Mediana").price(10).description("+50 HP").build());
            consumableRepository.save(Consumable.builder().name("Poción Grande").price(15).description("Full HP").build());
            consumableRepository.save(Consumable.builder().name("Escudo").price(8).description("Escudo 25% HP").build());
            System.out.println("Consumibles cargados.");
        }
    }
}