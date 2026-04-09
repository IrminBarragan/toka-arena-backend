package toka.tokagotchi.tokaarenabackend.missions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.missions.model.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
