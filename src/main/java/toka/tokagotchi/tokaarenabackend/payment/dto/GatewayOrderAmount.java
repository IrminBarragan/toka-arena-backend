package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatewayOrderAmount {
	private String value;
	private String currency;
}

