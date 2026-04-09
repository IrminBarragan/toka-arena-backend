package toka.tokagotchi.tokaarenabackend.tokagotchi.model;

import lombok.Getter;

@Getter
public enum TokaBaseStats {
    // Valores exactos del DOC FINAL (página 1)
    TOFU(100, 13, 30),
    MOCHI(90, 16, 20),
    HANA(125, 10, 45);

    private final int hp;
    private final int atk;
    private final int def;

    TokaBaseStats(int hp, int atk, int def) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
    }
}