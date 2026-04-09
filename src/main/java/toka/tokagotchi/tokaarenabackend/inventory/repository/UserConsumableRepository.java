package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.util.Optional;
/**
 * UserConsumableRepository: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


public interface UserConsumableRepository extends JpaRepository<UserConsumable, Long> {
    Optional<UserConsumable> findByOwnerAndConsumable(User user, Consumable consumable);
}
