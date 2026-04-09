package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

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