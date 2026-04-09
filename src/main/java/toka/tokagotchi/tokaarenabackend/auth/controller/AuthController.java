package toka.tokagotchi.tokaarenabackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.auth.service.ExternalAuthService;
import toka.tokagotchi.tokaarenabackend.security.jwt.JwtUtils;
import toka.tokagotchi.tokaarenabackend.user.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
/**
 * Expone endpoints de autenticacion para convertir el authCode externo
 * en un JWT interno consumible por el resto del backend.
 */
public class AuthController {

    private final ExternalAuthService externalAuthService;
    private final JwtUtils jwtUtils;

    /**
     * Intercambia un authCode de la Super App por un JWT interno.
     */
    @PostMapping("/login-superapp")
    public ResponseEntity<?> loginFromSuperApp(@RequestBody Map<String, String> request) {
        // Intentamos obtenerlo de ambas formas para evitar errores del cliente
        String authCode = request.get("authcode");
        if (authCode == null) {
            authCode = request.get("authCode");
        }

        if (authCode == null || authCode.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El authcode es obligatorio"));
        }

        try {
            // Al pasar "DEBUG", ExternalAuthService entrará en el modo simulado
            User user = externalAuthService.authenticateWithTalentLand(authCode);

            String internalToken = jwtUtils.generateTokenFromUserId(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("accessToken", internalToken);
            response.put("tokenType", "Bearer");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "tf", user.getTf(),
                    "hasFirstToka", user.isFirstToka()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Fallo en la autenticación",
                    "error", e.getMessage()
            ));
        }
    }
}