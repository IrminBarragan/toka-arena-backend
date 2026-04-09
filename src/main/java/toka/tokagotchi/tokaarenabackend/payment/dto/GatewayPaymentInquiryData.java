package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;

@Data
public class GatewayPaymentInquiryData {
    private String paymentId;
    private String paymentRequestId;
    private double paymentAmount;
    private String paymentTime;
    private String paymentCreateTime;
    private String paymentStatus;
    private String paymentResultCode;
    private String paymentResultMessage;
    private String paymentMethod;
    private String cardNumber;
}

