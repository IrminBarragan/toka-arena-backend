package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;

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

