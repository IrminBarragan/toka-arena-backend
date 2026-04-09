package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInquiryResult {
    private GatewayApiResponse<GatewayPaymentInquiryData> gatewayResponse;
    private boolean rewardApplied;
    private String rewardMessage;
}

