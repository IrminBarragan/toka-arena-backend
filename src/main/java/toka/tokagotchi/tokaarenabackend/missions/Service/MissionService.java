package toka.tokagotchi.tokaarenabackend.missions.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.missions.model.Mission;
import toka.tokagotchi.tokaarenabackend.missions.model.UserMission;
import toka.tokagotchi.tokaarenabackend.missions.repository.MissionRepository;
import toka.tokagotchi.tokaarenabackend.missions.repository.UserMissionRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final UserMissionRepository userMissionRepo;
    private final MissionRepository missionRepo;
    private final UserRepository userRepo;

    @Transactional
    public List<UserMission> getMissionsForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        List<UserMission> userMissions = userMissionRepo.findByUserId(userId);

        // Si no tiene misiones o la última actualización fue hace más de 24h, reiniciamos
        if (userMissions.isEmpty() || userMissions.get(0).getLastUpdated().isBefore(LocalDateTime.now().minusHours(24))) {
            userMissionRepo.deleteByUserId(userId);
            List<Mission> allMissions = missionRepo.findAll();
            userMissions = allMissions.stream().map(m -> userMissionRepo.save(
                    UserMission.builder().user(user).mission(m).currentProgress(0).completed(false).lastUpdated(LocalDateTime.now()).build()
            )).collect(Collectors.toList());
        }
        return userMissions;
    }

    @Transactional
    public void updateProgress(Long userId, String type) {
        List<UserMission> missions = userMissionRepo.findByUserIdAndMissionType(userId, type);
        // También actualizamos la misión de "CUIDADO_GENERAL" si aplica
        if (List.of("ALIMENTAR", "JUGAR", "BAÑAR").contains(type)) {
            missions.addAll(userMissionRepo.findByUserIdAndMissionType(userId, "CUIDADO_GENERAL"));
        }

        for (UserMission um : missions) {
            if (!um.isCompleted()) {
                um.setCurrentProgress(um.getCurrentProgress() + 1);
                if (um.getCurrentProgress() >= um.getMission().getRequiredAmount()) {
                    um.setCompleted(true);
                    User user = um.getUser();
                    user.setTf(user.getTf() + um.getMission().getRewardTf()); //
                    userRepo.save(user);
                }
                userMissionRepo.save(um);
            }
        }
    }
}