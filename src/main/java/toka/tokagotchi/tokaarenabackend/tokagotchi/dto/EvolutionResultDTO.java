package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EvolutionResultDTO {
    private boolean success;
    private String message;
    private TokagotchiResponse toka; // Usamos el DTO que ya tienes
}