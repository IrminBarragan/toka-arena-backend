package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatewayPaymentCreateRequest {
    private String userId;
    private String orderTitle;
    private GatewayOrderAmount orderAmount;
}

