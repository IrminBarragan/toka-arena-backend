package toka.tokagotchi.tokaarenabackend.battle.model;

import jakarta.persistence.*;
import lombok.*;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.time.LocalDateTime;
/**
 * Battle: componente del modulo `battle`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Entity
@Table(name = "battles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User player1;

    @ManyToOne
    private User player2;

    @ManyToOne
    private User winner;

    private String mode; // NORMAL o BET

    private LocalDateTime createdAt;
}