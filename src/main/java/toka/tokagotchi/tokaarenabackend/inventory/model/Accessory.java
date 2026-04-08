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

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private AccessoryType type;

    @Column(nullable = false)
    private String assetPath;

    @Column(nullable = false)
    private double cost;
}