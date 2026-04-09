package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/**
 * EvolutionResultDTO: componente del modulo `tokagotchi`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@AllArgsConstructor
@Builder
public class EvolutionResultDTO {
    private boolean success;
    private String message;
    private TokagotchiResponse toka; // Usamos el DTO que ya tienes
}