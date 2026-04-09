package toka.tokagotchi.tokaarenabackend.inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessoryDTO {
    private long id;
    private String name;
    private Boolean equipped;

}
