package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.util.Optional;

public interface UserConsumableRepository extends JpaRepository<UserConsumable, Long> {
    Optional<UserConsumable> findByOwnerAndConsumable(User user, Consumable consumable);
}
