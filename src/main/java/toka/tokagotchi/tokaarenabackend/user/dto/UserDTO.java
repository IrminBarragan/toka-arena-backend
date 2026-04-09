package toka.tokagotchi.tokaarenabackend.user.dto;

import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.inventory.dto.AccessoryDTO;
import toka.tokagotchi.tokaarenabackend.inventory.dto.ConsumableDTO;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;

import java.util.List;
/**
 * UserDTO: componente del modulo `user`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private double tf;
    private boolean firstToka;
    private TokagotchiResponse tokagotchiActivo;

    private List<TokagotchiResponse> tokagotchis;
    private List<AccessoryDTO> accessories;
    private List<ConsumableDTO> consumables;
}
