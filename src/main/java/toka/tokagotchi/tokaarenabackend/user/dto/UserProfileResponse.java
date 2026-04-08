package toka.tokagotchi.tokaarenabackend.user.dto;

import lombok.Builder;
import lombok.Data;
import toka.tokagotchi.tokaarenabackend.tokagotchi.dto.TokagotchiResponse;

import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private String username;
    private double tf;

    private List<TokagotchiResponse> tokagotchis;
    private List<AccessoryDTO> accessories;
    private List<ConsumableDTO> consumables;

    @Data
    @Builder
    public static class AccessoryDTO {
        private Long id;
        private String name;
        private String type;
        private boolean equipped;
    }

    @Data
    @Builder
    public static class ConsumableDTO {
        private Long id;
        private String name;
        private int quantity;
    }
}