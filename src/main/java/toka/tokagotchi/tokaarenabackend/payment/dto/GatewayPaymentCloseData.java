package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;
/**
 * GatewayPaymentCloseData: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class GatewayPaymentCloseData {
    private GatewayCloseResult result;
    private String paymentId;

    @Data
    public static class GatewayCloseResult {
        private String resultCode;
        private String resultMessage;
        private String resultStatus;
    }
}

