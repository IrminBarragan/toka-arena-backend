package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * PaymentCloseRequest: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class PaymentCloseRequest {
    @NotBlank
    private String paymentId;
}

