package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;

import java.util.Optional;

public interface UserAccessoryRepository extends JpaRepository<UserAccessory, Long> {
    Optional<UserAccessory> findByAccessory_Id(long id);

}
