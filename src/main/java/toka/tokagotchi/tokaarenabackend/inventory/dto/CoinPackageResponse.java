package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CoinPackageResponse {
    private String id;
    private int tfAmount;
    private double priceUsd;
    private String description;
    private String extraReward;
}