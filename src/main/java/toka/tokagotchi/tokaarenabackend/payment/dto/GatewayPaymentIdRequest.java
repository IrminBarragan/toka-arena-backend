package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatewayPaymentIdRequest {
    private String paymentId;
}

