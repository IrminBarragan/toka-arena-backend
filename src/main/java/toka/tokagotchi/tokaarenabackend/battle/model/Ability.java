package toka.tokagotchi.tokaarenabackend.battle.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;

@Entity
@Table(name = "abilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int energyCost;
    private Double multiplier;
    private String description;
    private boolean isSignature;
    private boolean scaleWithRarity;

    @Enumerated(EnumType.STRING)
    private Species species;
}
