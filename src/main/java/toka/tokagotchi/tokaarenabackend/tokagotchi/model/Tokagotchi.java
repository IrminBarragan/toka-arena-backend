package toka.tokagotchi.tokaarenabackend.tokagotchi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.common.enums.Rarity;
import toka.tokagotchi.tokaarenabackend.common.enums.Species;
import toka.tokagotchi.tokaarenabackend.inventory.model.Accessory;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokagotchis")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
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
    private int cp;

    private LocalDateTime evolutionCooldown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"tokagotchis", "accessories", "consumables", "password", "email"})
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipped_head_id")
    private Accessory equippedHead;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipped_body_id")
    private Accessory equippedBody;
}