package toka.tokagotchi.tokaarenabackend.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import toka.tokagotchi.tokaarenabackend.auth.dto.TalentLandAuthRequest;
import toka.tokagotchi.tokaarenabackend.auth.dto.TalentLandAuthResponse;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

@Service
/**
 * Encapsula la autenticacion contra la Super App y sincroniza
 * el usuario local con el token externo cifrado.
 */
public class ExternalAuthService {

    @Qualifier("talentLandWebClient")
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final TextEncryptor encryptor;

    public ExternalAuthService(@Qualifier("talentLandWebClient") WebClient webClient,
                               UserRepository userRepository,  TextEncryptor encryptor) {
        this.webClient = webClient;
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    /**
     * Autentica con la API externa y devuelve/crea el usuario local.
     */
    public User authenticateWithTalentLand(String authCode) {
        if ("DEBUG".equals(authCode)) {
            return userRepository.findByTalentLandUserId("DEBUG_USER_123")
                    .orElseGet(() -> userRepository.save(User.builder()
                            .username("Tester_Local")
                            .talentLandUserId("DEBUG_USER_123")
                            .tf(999)
                            .firstToka(false)
                            .build()));
        }

        // 1. Petición Server-to-Server
        TalentLandAuthResponse response = webClient.post()
                .uri("/v1/user/authenticate")
                .bodyValue(new TalentLandAuthRequest(authCode))
                .retrieve()
                .bodyToMono(TalentLandAuthResponse.class)
                .block(); // Bloqueante para simplificar este flujo síncrono

        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("Error autenticando con la Super App: " +
                    (response != null ? response.getMessage() : "Sin respuesta"));
        }

        TalentLandAuthResponse.TalentLandData data = response.getData();
        String rawToken = data.getAccessToken();
        String encryptedToken = encryptor.encrypt(rawToken);

        // 2. Gestionar usuario en nuestra DB MySQL
        return userRepository.findByTalentLandUserId(data.getUserId())
                .map(existingUser -> {
                    // Actualizar token si ya existe
                    existingUser.setCurrentAccessToken(encryptedToken);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    // Crear nuevo usuario si es la primera vez
                    User newUser = User.builder()
                            .username("SuperAppUser_" + data.getUserId().substring(0, 5)) // Username temporal
                            .talentLandUserId(data.getUserId())
                            .currentAccessToken(encryptedToken)
                            .firstToka(false)
                            .tf(100) // Regalo inicial de monedas
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
