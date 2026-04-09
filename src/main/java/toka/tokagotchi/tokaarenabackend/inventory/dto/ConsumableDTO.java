package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.Builder;
import lombok.Data;
/**
 * ConsumableDTO: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class ConsumableDTO {
    private String name;
    private int  quantity;
    private String description;
}
