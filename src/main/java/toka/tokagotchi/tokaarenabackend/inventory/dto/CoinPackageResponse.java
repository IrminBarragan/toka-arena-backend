package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;
/**
 * CoinPackageResponse: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@AllArgsConstructor
@Builder
public class CoinPackageResponse {
    private String id;
    private int tfAmount;
    private double priceMxn;
    private String description;
    private String extraReward;
    private Tokagotchi tokagotchiRegalo;
}