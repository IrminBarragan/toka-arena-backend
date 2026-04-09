package toka.tokagotchi.tokaarenabackend.battle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;

import java.util.List;
/**
 * AbilityRepository: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


public interface AbilityRepository extends JpaRepository<Ability, Long> {
    List<Ability> findBySpecies(Species species);
}
