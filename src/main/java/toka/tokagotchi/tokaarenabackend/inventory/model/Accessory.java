package toka.tokagotchi.tokaarenabackend.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.common.enums.AccessoryType;

@Entity
@Table(name = "accessories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // "Casco", "Supercapa"

    @Enumerated(EnumType.STRING)
    private AccessoryType type; // HEAD / BODY

    private String assetPath;
}