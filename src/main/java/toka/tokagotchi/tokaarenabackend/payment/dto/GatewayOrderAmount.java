package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;
/**
 * GatewayOrderAmount: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class GatewayOrderAmount {
	private String value;
	private String currency;
}

