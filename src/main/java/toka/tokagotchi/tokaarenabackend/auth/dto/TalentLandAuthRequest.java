package toka.tokagotchi.tokaarenabackend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request enviado a la Super App para validar un authCode
 * y obtener identidad/token externo del usuario.
 */
@Data
@AllArgsConstructor
public class TalentLandAuthRequest {

    /** Codigo temporal de autenticacion emitido por la Super App. */
    private String authCode;
}
