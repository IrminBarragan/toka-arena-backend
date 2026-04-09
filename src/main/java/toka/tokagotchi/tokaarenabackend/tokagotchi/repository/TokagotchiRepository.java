package toka.tokagotchi.tokaarenabackend.tokagotchi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

import java.util.Optional;

public interface TokagotchiRepository extends JpaRepository<Tokagotchi, Long> {
	Optional<Tokagotchi> findByIdAndOwnerId(Long id, Long ownerId);
}
