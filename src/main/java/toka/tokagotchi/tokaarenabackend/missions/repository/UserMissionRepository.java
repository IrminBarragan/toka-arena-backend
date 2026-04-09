package toka.tokagotchi.tokaarenabackend.missions.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import toka.tokagotchi.tokaarenabackend.missions.model.UserMission;

import java.util.List;

public interface UserMissionRepository extends JpaRepositoryImplementation<UserMission, Long> {

    List<UserMission> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    List<UserMission> findByUserIdAndMissionType(Long userId, String type);
}
