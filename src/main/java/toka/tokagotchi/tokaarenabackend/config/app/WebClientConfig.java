package toka.tokagotchi.tokaarenabackend.config.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.reactive.function.client.WebClient;
/**
 * WebClientConfig: componente del modulo `config`.
 * Su responsabilidad principal es definir configuraciones de la aplicacion.
 */


@Configuration
public class WebClientConfig {

    @Value("${talentland.api.base-url}")
    private String baseUrl;

    @Value("${talentland.api.app-id}")
    private String appId;

    @Bean(name = "talentLandWebClient")
    public WebClient talentLandWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-App-Id", appId)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        // TODO: CAMBIAR LLAVE SECRETA A VARIABLE DE ENTORNO
        return Encryptors.delux("mi_clave_secreta_super_segura", "5c0744940b5c369b");
    }
}