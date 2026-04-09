package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * PaymentAmountRequest: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class PaymentAmountRequest {
	@NotBlank
	private String value;

	@NotBlank
	private String currency;
}

