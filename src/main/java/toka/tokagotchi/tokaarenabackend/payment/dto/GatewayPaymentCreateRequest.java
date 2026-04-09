package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;
/**
 * GatewayPaymentCreateRequest: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class GatewayPaymentCreateRequest {
    private String userId;
    private String orderTitle;
    private GatewayOrderAmount orderAmount;
}

