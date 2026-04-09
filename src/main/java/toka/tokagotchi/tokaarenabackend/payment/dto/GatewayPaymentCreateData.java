package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;
/**
 * GatewayPaymentCreateData: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class GatewayPaymentCreateData {
	private String paymentId;
	private String paymentUrl;
}

