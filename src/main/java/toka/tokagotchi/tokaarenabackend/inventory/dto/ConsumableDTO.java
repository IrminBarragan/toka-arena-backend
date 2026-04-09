package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumableDTO {
    private String name;
    private int  quantity;
    private String description;
}
