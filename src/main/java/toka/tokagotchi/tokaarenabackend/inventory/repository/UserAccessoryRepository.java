package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;

public interface UserAccessoryRepository extends JpaRepository<UserAccessory, Long> {
}
