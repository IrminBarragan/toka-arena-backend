package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentAmountRequest {
	@NotBlank
	private String value;

	@NotBlank
	private String currency;
}

