package toka.tokagotchi.tokaarenabackend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;

import java.util.Optional;
/**
 * UserAccessoryRepository: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


public interface UserAccessoryRepository extends JpaRepository<UserAccessory, Long> {
	Optional<UserAccessory> findByAccessory_Id(long id);
	Optional<UserAccessory> findByOwnerIdAndAccessoryId(Long ownerId, Long accessoryId);
}
