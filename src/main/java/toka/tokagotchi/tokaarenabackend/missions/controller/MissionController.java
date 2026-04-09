package toka.tokagotchi.tokaarenabackend.missions.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.missions.Service.MissionService;
import toka.tokagotchi.tokaarenabackend.missions.model.UserMission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public ResponseEntity<?> getMyMissions() {
        // Extraer ID del usuario desde el Doble Token
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = Long.parseLong(userIdStr);

        List<UserMission> missions = missionService.getMissionsForUser(userId);

        // Calcular progreso general para la UI de la imagen
        long completedCount = missions.stream().filter(UserMission::isCompleted).count();

        return ResponseEntity.ok(Map.of(
                "summary", completedCount + "/" + missions.size() + " completadas",
                "missions", missions.stream().map(this::mapToDto).collect(Collectors.toList())
        ));
    }

    private Map<String, Object> mapToDto(UserMission um) {
        return Map.of(
                "id", um.getId(),
                "description", um.getMission().getDescription(),
                "currentProgress", um.getCurrentProgress(),
                "requiredAmount", um.getMission().getRequiredAmount(),
                "rewardTf", um.getMission().getRewardTf(),
                "completed", um.isCompleted(),
                "percentage", (um.getCurrentProgress() * 100) / um.getMission().getRequiredAmount()
        );
    }
}