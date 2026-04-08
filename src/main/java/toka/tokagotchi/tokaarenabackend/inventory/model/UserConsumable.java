package toka.tokagotchi.tokaarenabackend.inventory.model;


import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.user.model.User;

@Entity
@Table(name = "user_consumables")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserConsumable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumable_id", nullable = false)
    private Consumable consumable;

    private int quantity;
}