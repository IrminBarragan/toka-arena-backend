package toka.tokagotchi.tokaarenabackend.config.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.battle.repository.AbilityRepository;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AbilityRepository abilityRepository;

    @Override
    public void run(String... args) {

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
}