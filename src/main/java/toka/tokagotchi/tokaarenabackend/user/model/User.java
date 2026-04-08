package toka.tokagotchi.tokaarenabackend.user.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserAccessory;
import toka.tokagotchi.tokaarenabackend.inventory.model.UserConsumable;
import toka.tokagotchi.tokaarenabackend.tokagotchi.model.Tokagotchi;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    private double tf;

    private boolean firstToka = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tokagotchi> tokagotchis;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAccessory> accessories;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserConsumable> consumables;
}