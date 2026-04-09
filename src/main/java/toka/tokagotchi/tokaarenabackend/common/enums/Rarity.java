package toka.tokagotchi.tokaarenabackend.common.enums;

import lombok.Getter;

@Getter
public enum Rarity {
    COMMON(1.00),
    RARE(1.15 ),
    EPIC(1.35),
    LEGENDARY(1.55);

    private final double multiplier;

    Rarity(double multiplier) {
        this.multiplier = multiplier;
    }
}
