package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;

import java.util.Optional;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    Optional<Accessory> findByName(String name);
}
