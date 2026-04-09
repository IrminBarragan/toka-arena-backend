package toka.tokagotchi.tokaarenabackend.inventory.constant;

import toka.tokagotchi.tokaarenabackend.inventory.dto.CoinPackageResponse;

import java.util.List;

public class ShopConstants {
    public static final List<CoinPackageResponse> TF_PACKAGES = List.of(
            CoinPackageResponse.builder()
                    .id("pkg_1")
                    .tfAmount(20)
                    .priceMxn(19.0)
                    .description("Paquete inicial de Toka Feed")
                    .build(),
            CoinPackageResponse.builder()
                    .id("pkg_2")
                    .tfAmount(50)
                    .priceMxn(45.0)
                    .description("Paquete de aventurero")
                    .build(),
            CoinPackageResponse.builder()
                    .id("pkg_3")
                    .tfAmount(120)
                    .priceMxn(99.0)
                    .description("Paquete Pro")
                    .extraReward("Tokagotchi Común incluido")
                    .build(),
            CoinPackageResponse.builder()
                    .id("pkg_4")
                    .tfAmount(260)
                    .priceMxn(199.0)
                    .description("Paquete Maestro")
                    .extraReward("Tokagotchi Raro incluido")
                    .build()
    );
}