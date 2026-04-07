package toka.tokagotchi.tokaarenabackend.tokagotchi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TokagotchiResponse {

    private String id;
    private String nombre;
    private String especie;
    private String rareza;

    private Stats stats;
    private int felicidad;

    private List<Habilidad> habilidades;
    private Accesorios accesorios;
    private Assets assets;

    @Data
    @Builder
    public static class Stats {
        private int hp;
        private int atk;
        private int def;
        private int nrg;
    }

    @Data
    @Builder
    public static class Habilidad {
        private String id;
        private String nombre;
        private int costoNRG;
        private Double multiplicador;
        private String descripcion;
        private boolean esSignature;
    }

    @Data
    @Builder
    public static class Accesorios {
        private Object cabeza;
        private Object cuerpo;
    }

    @Data
    @Builder
    public static class Assets {
        private String armatureKey;
        private String texPng;
        private String texJson;
        private String skeJson;
    }
}
