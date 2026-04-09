package toka.tokagotchi.tokaarenabackend.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayApiResponse;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayPaymentCloseData;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayPaymentCreateData;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayPaymentCreateRequest;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayPaymentIdRequest;
import toka.tokagotchi.tokaarenabackend.payment.dto.GatewayPaymentInquiryData;

@Component
@RequiredArgsConstructor
public class PaymentGatewayClient {

    @Qualifier("talentLandWebClient")
    private final WebClient talentLandWebClient;

    @Value("${talentland.api.alipay-merchant-code}")
    private String merchantCode;

    public GatewayApiResponse<GatewayPaymentCreateData> createPayment(
            String bearerToken,
            GatewayPaymentCreateRequest request
    ) {
        try {
            return talentLandWebClient.post()
                    .uri("/v1/payment/create")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .header("Alipay-MerchantCode", merchantCode)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<GatewayApiResponse<GatewayPaymentCreateData>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Error en pasarela de pago: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo conectar con la pasarela de pago");
        }
    }

    public GatewayApiResponse<GatewayPaymentInquiryData> inquirePayment(String bearerToken, String paymentId) {
        try {
            return talentLandWebClient.post()
                    .uri("/v1/payment/inquiry")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .bodyValue(GatewayPaymentIdRequest.builder().paymentId(paymentId).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<GatewayApiResponse<GatewayPaymentInquiryData>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Error en pasarela de pago: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo conectar con la pasarela de pago");
        }
    }

    public GatewayApiResponse<GatewayPaymentCloseData> closePayment(String bearerToken, String paymentId) {
        try {
            return talentLandWebClient.post()
                    .uri("/v1/payment/close")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .bodyValue(GatewayPaymentIdRequest.builder().paymentId(paymentId).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<GatewayApiResponse<GatewayPaymentCloseData>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Error en pasarela de pago: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo conectar con la pasarela de pago");
        }
    }
}




