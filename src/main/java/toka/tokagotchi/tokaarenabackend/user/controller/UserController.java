package toka.tokagotchi.tokaarenabackend.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.inventory.dto.AccessoryDTO;
import toka.tokagotchi.tokaarenabackend.inventory.dto.ConsumableDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.mapper.TokagotchiMapper;
import toka.tokagotchi.tokaarenabackend.user.dto.UserDTO;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final TokagotchiMapper tokaMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyFullProfile() {
        // 1. Obtener ID desde el Token B
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = Long.parseLong(userIdStr);

        // 2. Buscar usuario con sus relaciones
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Mapear y devolver la respuesta
        UserDTO response = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .tf(user.getTf())
                .firstToka(user.isFirstToka())
                // Convertimos la lista de Tokagotchis usando el formato que incluye habilidades
                .tokagotchis(user.getTokagotchis().stream()
                        .map(tokaMapper::toResponse)
                        .collect(Collectors.toList()))
                // Mapeamos accesorios del inventario
                .accessories(user.getAccessories().stream()
                        .map(ua -> AccessoryDTO.builder()
                                .id(ua.getAccessory().getId())
                                .name(ua.getAccessory().getName())
                                .equipped(ua.isEquipped())
                                .build())
                        .collect(Collectors.toList()))
                // Mapeamos consumibles
                .consumables(user.getConsumables().stream()
                        .map(uc -> ConsumableDTO.builder()
                                .name(uc.getConsumable().getName())
                                .quantity(uc.getQuantity())
                                .description(uc.getConsumable().getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.ok(response);
    }
}
