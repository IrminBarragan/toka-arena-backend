package toka.tokagotchi.tokaarenabackend.auth.dto;

import lombok.Data;

@Data
public class TalentLandAuthResponse {
    private boolean success;
    private int statusCode;
    private String message;
    private TalentLandData data;

    @Data
    public static class TalentLandData {
        private String userId;
        private String accessToken;
        private String tokenType;
        private int expiresIn;
    }
}