package toka.tokagotchi.tokaarenabackend.shop.ShopService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.Consumable;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.inventory.repository.AccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.ConsumableRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserAccessoryRepository;
import toka.tokagotchi.tokaarenabackend.inventory.repository.UserConsumableRepository;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final AccessoryRepository accessoryRepository;
    private final ConsumableRepository consumableRepository;
    private final UserAccessoryRepository userAccessoryRepository;
    private final UserConsumableRepository userConsumableRepository;

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

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error de sesión: Usuario no encontrado"));
    }
}