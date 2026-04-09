package toka.tokagotchi.tokaarenabackend.missions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Mission: componente del modulo `missions`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int requiredAmount; // Cantidad necesaria (ej: 3 para acciones de cuidado)
    private double rewardTf;    // Recompensa en TF
    private String type;        // PvP, ALIMENTAR, JUGAR, BAÑAR, CUIDADO_GENERAL
}
