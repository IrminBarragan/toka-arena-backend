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
    private String id;

    private String nombre;
    private int costoNRG;
    private Double multiplicador;
    private String descripcion;
    private boolean esSignature;
    private boolean scaleWithRarity;

    @Enumerated(EnumType.STRING)
    private Species species;
}
