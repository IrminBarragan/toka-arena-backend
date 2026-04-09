package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentCloseRequest {
    @NotBlank
    private String paymentId;
}

