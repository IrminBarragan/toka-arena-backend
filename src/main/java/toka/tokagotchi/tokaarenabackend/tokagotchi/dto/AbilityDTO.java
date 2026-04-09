package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.Builder;
import lombok.Data;
/**
 * AbilityDTO: componente del modulo `tokagotchi`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class AbilityDTO {
    private Long id;
    private String name;
    private int energyCost;
    private Double multiplier;
    private String description;
}
