package toka.tokagotchi.tokaarenabackend.tokagotchi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

public interface TokagotchiRepository extends JpaRepository<Tokagotchi, Long> {
}
