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
    private String id; // "zarpazo", "agilidad", etc.

    private String nombre;

    private int costoNRG;

    private Double multiplicador; // puede ser null

    private String descripcion;

    private boolean esSignature;

    @Enumerated(EnumType.STRING)
    private Species species; // a qué especie pertenece
}
