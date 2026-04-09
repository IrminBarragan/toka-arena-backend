package toka.tokagotchi.tokaarenabackend.missions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.missions.model.Mission;

/**
 * Repositorio base para catalogo de misiones disponibles en el juego.
 */
public interface MissionRepository extends JpaRepository<Mission, Long> {
}
