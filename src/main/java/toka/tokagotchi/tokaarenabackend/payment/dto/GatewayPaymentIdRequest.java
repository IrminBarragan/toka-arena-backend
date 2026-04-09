package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;
/**
 * GatewayPaymentIdRequest: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class GatewayPaymentIdRequest {
    private String paymentId;
}

