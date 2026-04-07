package toka.tokagotchi.tokaarenabackend.user.model;

import jakarta.persistence.*;
import lombok.*;
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

    @Column(nullable = false)
    private boolean firstToka = false;

    // Relación con tokas
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Tokagotchi> tokagotchis;

    // Recursos (ej: esencias)
    private int essences;

    private boolean enabled = true;
}