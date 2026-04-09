package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.Builder;
import lombok.Data;
/**
 * AccessoryDTO: componente del modulo `inventory`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class AccessoryDTO {
    private long id;
    private String name;
    private Boolean equipped;
}
