package toka.tokagotchi.tokaarenabackend.tokagotchi.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.user.model.User;

@Entity
@Table(name = "tokagotchis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tokagotchi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Species species;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    private int hp;
    private int atk;
    private int def;

    private int happiness;
    private int cp;

    // Relación con usuario
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}