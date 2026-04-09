package toka.tokagotchi.tokaarenabackend.missions.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.time.LocalDateTime;
/**
 * UserMission: componente del modulo `missions`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Entity
@Table(name = "user_missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "mission_id")
    private Mission mission;

    private int currentProgress;
    private boolean completed;
    private LocalDateTime lastUpdated;
}