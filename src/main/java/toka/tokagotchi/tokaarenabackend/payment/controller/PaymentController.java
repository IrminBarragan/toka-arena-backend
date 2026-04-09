package toka.tokagotchi.tokaarenabackend.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.payment.dto.*;
import toka.tokagotchi.tokaarenabackend.payment.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<GatewayApiResponse<GatewayPaymentCreateData>> create(
            @Valid @RequestBody PaymentCreateRequest request
    ) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @PostMapping("/inquiry")
    public ResponseEntity<PaymentInquiryResult> inquiry(@Valid @RequestBody PaymentInquiryRequest request) {
        return ResponseEntity.ok(paymentService.inquiry(request));
    }

    @PostMapping("/close")
    public ResponseEntity<GatewayApiResponse<GatewayPaymentCloseData>> close(
            @Valid @RequestBody PaymentCloseRequest request
    ) {
        return ResponseEntity.ok(paymentService.close(request));
    }
}

