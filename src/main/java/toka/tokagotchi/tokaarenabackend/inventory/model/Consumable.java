package toka.tokagotchi.tokaarenabackend.inventory.model;

import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consumables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consumable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String effect;

    private int cost;
}