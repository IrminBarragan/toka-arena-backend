package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;

import java.util.Optional;
/**
 * AccessoryRepository: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    Optional<Accessory> findByName(String name);
}
