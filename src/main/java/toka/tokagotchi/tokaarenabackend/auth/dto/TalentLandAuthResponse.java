package toka.tokagotchi.tokaarenabackend.auth.dto;

import lombok.Data;

/**
 * Respuesta estandar que devuelve la Super App al autenticar un usuario.
 */
@Data
public class TalentLandAuthResponse {
    private boolean success;
    private int statusCode;
    private String message;
    private TalentLandData data;

    /**
     * Payload con identidad y token de acceso externos.
     */
    @Data
    public static class TalentLandData {
        private String userId;
        private String accessToken;
        private String tokenType;
        private int expiresIn;
    }
}