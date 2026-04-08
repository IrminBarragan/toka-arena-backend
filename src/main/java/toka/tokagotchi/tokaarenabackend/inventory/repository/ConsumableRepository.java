package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;

public interface ConsumableRepository extends JpaRepository<Consumable, Long> {
}
