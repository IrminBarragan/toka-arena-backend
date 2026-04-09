package toka.tokagotchi.tokaarenabackend.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateRequest {
    @NotBlank
    private String orderTitle;

    @Valid
    @NotNull
    private PaymentAmountRequest orderAmount;

    // Optional: used to grant TF package after SUCCESS inquiry.
    private String packageId;
}

