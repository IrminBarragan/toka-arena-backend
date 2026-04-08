package toka.tokagotchi.tokaarenabackend.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.inventory.constant.ShopConstants;
import toka.tokagotchi.tokaarenabackend.inventory.dto.CoinPackageResponse;
import toka.tokagotchi.tokaarenabackend.shop.ShopService.ShopService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    // Obtener los paquetes de monedas (Constantes)
    @GetMapping("/packages")
    public ResponseEntity<List<CoinPackageResponse>> getCoinPackages() {
        return ResponseEntity.ok(ShopConstants.TF_PACKAGES);
    }

    // Comprar Accesorio
    @PostMapping("/buy/accessory/{id}")
    public ResponseEntity<?> buyAccessory(@PathVariable Long id) {
        shopService.buyAccessory(id);
        return ResponseEntity.ok().body("{\"message\": \"Accesorio adquirido correctamente\"}");
    }

    // Comprar Consumibles (Pociones)
    @PostMapping("/buy/consumable/{id}")
    public ResponseEntity<?> buyConsumable(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
        shopService.buyConsumable(id, quantity);
        return ResponseEntity.ok().body("{\"message\": \"Compra de consumible(s) exitosa\"}");
    }
}