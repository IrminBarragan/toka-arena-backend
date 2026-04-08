package toka.tokagotchi.tokaarenabackend.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.user.dto.UserProfileResponse;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokagotchiMapper tokagotchiMapper;

    public UserProfileResponse getProfile() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return UserProfileResponse.builder()
                .username(user.getUsername())
                .tf(user.getTf())

                .tokagotchis(mapTokas(user))
                .accessories(mapAccessories(user))
                .consumables(mapConsumables(user))

                .build();
    }

    // =========================
    // TOKAS
    // =========================

    private List<TokagotchiResponse> mapTokas(User user) {
        return user.getTokagotchis()
                .stream()
                .map(tokagotchiMapper::toResponse)
                .toList();
    }

    // =========================
    // ACCESSORIES
    // =========================

    private List<UserProfileResponse.AccessoryDTO> mapAccessories(User user) {
        return user.getAccessories().stream()
                .map(a -> UserProfileResponse.AccessoryDTO.builder()
                        .id(a.getAccessory().getId())
                        .name(a.getAccessory().getName())
                        .type(a.getAccessory().getType().name())
                        .equipped(a.isEquipped())
                        .build()
                ).toList();
    }

    // =========================
    // CONSUMABLES
    // =========================

    private List<UserProfileResponse.ConsumableDTO> mapConsumables(User user) {
        return user.getConsumables().stream()
                .map(c -> UserProfileResponse.ConsumableDTO.builder()
                        .id(c.getConsumable().getId())
                        .name(c.getConsumable().getName())
                        .quantity(c.getQuantity())
                        .build()
                ).toList();
    }
}
