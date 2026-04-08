package toka.tokagotchi.tokaarenabackend.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.user.model.User;

@Entity
@Table(name = "user_accessories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Debe coincidir con mappedBy en User
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessory_id", nullable = false)
    private Accessory accessory;

    private boolean equipped;
}