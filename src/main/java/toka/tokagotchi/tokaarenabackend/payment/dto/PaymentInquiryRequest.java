package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * PaymentInquiryRequest: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class PaymentInquiryRequest {
    @NotBlank
    private String paymentId;
}

