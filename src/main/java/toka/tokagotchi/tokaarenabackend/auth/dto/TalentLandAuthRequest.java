package toka.tokagotchi.tokaarenabackend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TalentLandAuthRequest {
    private String authCode;
}
