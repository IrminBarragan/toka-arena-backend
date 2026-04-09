package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;

import java.util.Optional;
/**
 * ConsumableRepository: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


public interface ConsumableRepository extends JpaRepository<Consumable, Long> {
    Optional<Consumable> findByName(String name);
}
