package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Builder;
import lombok.Data;
/**
 * PaymentInquiryResult: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
@Builder
public class PaymentInquiryResult {
    private GatewayApiResponse<GatewayPaymentInquiryData> gatewayResponse;
    private boolean rewardApplied;
    private String rewardMessage;
}

