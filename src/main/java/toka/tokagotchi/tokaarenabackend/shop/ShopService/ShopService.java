package toka.tokagotchi.tokaarenabackend.shop.ShopService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.inventory.constant.ShopConstants;
import toka.tokagotchi.tokaarenabackend.inventory.dto.CoinPackageResponse;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.inventory.repository.AccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.ConsumableRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserAccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserConsumableRepository;
import toka.tokagotchi.tokaarenabackend.tokagotchi.service.TokagotchiService;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;
/**
 * ShopService: componente del modulo `shop`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Service
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final AccessoryRepository accessoryRepository;
    private final ConsumableRepository consumableRepository;
    private final UserAccessoryRepository userAccessoryRepository;
    private final UserConsumableRepository userConsumableRepository;
    private final TokagotchiService tokagotchiService;

    @Transactional
    public void buyAccessory(Long accessoryId) {
        User user = getAuthenticatedUser();

        Accessory accessory = accessoryRepository.findById(accessoryId)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado en el catálogo"));

        if (user.getTf() < accessory.getPrice()) {
            throw new RuntimeException("Saldo insuficiente de TF (Toka Feed)");
        }

        // 1. Descontar TF
        user.setTf(user.getTf() - accessory.getPrice());
        userRepository.save(user);

        // 2. Crear el ítem en el inventario del usuario
        UserAccessory userAccessory = UserAccessory.builder()
                .owner(user)
                .accessory(accessory)
                .equipped(false)
                .build();

        userAccessoryRepository.save(userAccessory);
    }

    @Transactional
    public void buyConsumable(Long consumableId, int quantity) {
        if (quantity <= 0) throw new RuntimeException("La cantidad debe ser mayor a 0");

        User user = getAuthenticatedUser();
        Consumable consumable = consumableRepository.findById(consumableId)
                .orElseThrow(() -> new RuntimeException("Consumible no encontrado"));

        double totalCost = consumable.getPrice() * quantity;

        if (user.getTf() < totalCost) {
            throw new RuntimeException("Saldo insuficiente de TF");
        }

        // 1. Descontar TF
        user.setTf(user.getTf() - totalCost);
        userRepository.save(user);

        // 2. Lógica de "Stacking" (Si ya tiene, sumar cantidad; si no, crear)
        UserConsumable userConsumable = userConsumableRepository
                .findByOwnerAndConsumable(user, consumable)
                .orElse(UserConsumable.builder()
                        .owner(user)
                        .consumable(consumable)
                        .quantity(0)
                        .build());

        userConsumable.setQuantity(userConsumable.getQuantity() + quantity);
        userConsumableRepository.save(userConsumable);
    }

    @Transactional
    public String processPackagePurchase(Long userId, String packageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Buscar el paquete en las constantes
        CoinPackageResponse pkg = ShopConstants.TF_PACKAGES.stream()
                .filter(p -> p.getId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paquete no válido"));

        // 2. Acreditar los TF
        user.setTf(user.getTf() + pkg.getTfAmount());
        userRepository.save(user);

        // 3. Procesar Recompensas Extra
        String rewardMessage = "";
        if ("pkg_3".equals(packageId)) {
            // Paquete Pro incluye un Tokagotchi Común
            tokagotchiService.awardExtraToka(user, Rarity.COMMON);
            rewardMessage = " ¡Y recibiste un Tokagotchi Común!";
        } else if ("pkg_4".equals(packageId)) {
            // Paquete Maestro incluye un Tokagotchi Raro
            tokagotchiService.awardExtraToka(user, Rarity.RARE);
            rewardMessage = " ¡Y recibiste un Tokagotchi Raro!";
        }

        return "Compra de " + pkg.getTfAmount() + " TF exitosa." + rewardMessage;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        final Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Token de usuario invalido");
        }

        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Error de sesión: Usuario no encontrado"));
    }
}