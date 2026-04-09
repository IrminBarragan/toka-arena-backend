package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbilityDTO {
    private Long id;
    private String name;
    private int energyCost;
    private Double multiplier;
    private String description;
}
