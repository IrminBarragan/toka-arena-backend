package toka.tokagotchi.tokaarenabackend.battle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.battle.model.Ability;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;

import java.util.List;

public interface AbilityRepository extends JpaRepository<Ability, String> {
    List<Ability> findBySpecies(Species species);
}
