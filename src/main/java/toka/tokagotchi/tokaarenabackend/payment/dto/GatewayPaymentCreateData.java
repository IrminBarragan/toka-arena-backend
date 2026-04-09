package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;

@Data
public class GatewayPaymentCreateData {
	private String paymentId;
	private String paymentUrl;
}

