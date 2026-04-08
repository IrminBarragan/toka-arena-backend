package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

}
