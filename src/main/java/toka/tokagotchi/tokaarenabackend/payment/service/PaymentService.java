package toka.tokagotchi.tokaarenabackend.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.inventory.constant.ShopConstants;
import toka.tokagotchi.tokaarenabackend.payment.dto.*;
import toka.tokagotchi.tokaarenabackend.payment.model.PaymentRecord;
import toka.tokagotchi.tokaarenabackend.payment.repository.PaymentRecordRepository;
import toka.tokagotchi.tokaarenabackend.shop.ShopService.ShopService;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

import java.time.LocalDateTime;
/**
 * PaymentService: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGatewayClient paymentGatewayClient;
    private final PaymentRecordRepository paymentRecordRepository;
    private final UserRepository userRepository;
    private final ShopService shopService;
    private final TextEncryptor encryptor;

    @Transactional
    public GatewayApiResponse<GatewayPaymentCreateData> create(PaymentCreateRequest request) {
        validateCreateRequest(request);

        User user = getAuthenticatedUser();
        String accessToken = getDecryptedAccessToken(user);

        GatewayPaymentCreateRequest gatewayRequest = GatewayPaymentCreateRequest.builder()
                .userId(user.getTalentLandUserId())
                .orderTitle(request.getOrderTitle().trim())
                .orderAmount(GatewayOrderAmount.builder()
                        .value(request.getOrderAmount().getValue().trim())
                        .currency(request.getOrderAmount().getCurrency().trim())
                        .build())
                .build();

        GatewayApiResponse<GatewayPaymentCreateData> response = paymentGatewayClient.createPayment(accessToken, gatewayRequest);

        if (response == null || response.getData() == null || response.getData().getPaymentId() == null) {
            throw new RuntimeException("Respuesta invalida de la pasarela de pago");
        }

        PaymentRecord record = paymentRecordRepository.findByPaymentId(response.getData().getPaymentId())
                .orElse(PaymentRecord.builder()
                        .paymentId(response.getData().getPaymentId())
                        .build());

        record.setUser(user);
        record.setPackageId(normalizePackageId(request.getPackageId()));
        record.setOrderTitle(request.getOrderTitle().trim());
        record.setOrderAmountValue(request.getOrderAmount().getValue().trim());
        record.setOrderCurrency(request.getOrderAmount().getCurrency().trim());
        record.setPaymentStatus("CREATED");
        paymentRecordRepository.save(record);

        return response;
    }

    @Transactional
    public PaymentInquiryResult inquiry(PaymentInquiryRequest request) {
        User user = getAuthenticatedUser();
        String accessToken = getDecryptedAccessToken(user);

        PaymentRecord record = paymentRecordRepository.findByPaymentId(request.getPaymentId().trim())
                .orElseThrow(() -> new RuntimeException("Pago no registrado en este backend"));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para consultar este pago");
        }

        GatewayApiResponse<GatewayPaymentInquiryData> response =
                paymentGatewayClient.inquirePayment(accessToken, request.getPaymentId().trim());

        if (response == null || response.getData() == null) {
            throw new RuntimeException("Respuesta invalida de la pasarela de pago");
        }

        record.setPaymentStatus(response.getData().getPaymentStatus());

        boolean rewardApplied = false;
        String rewardMessage = null;

        if ("SUCCESS".equalsIgnoreCase(response.getData().getPaymentStatus())
                && record.getPackageId() != null
                && !record.isRewardApplied()) {
            rewardMessage = shopService.processPackagePurchase(user.getId(), record.getPackageId());
            record.setRewardApplied(true);
            record.setRewardAppliedAt(LocalDateTime.now());
            record.setRewardMessage(rewardMessage);
            rewardApplied = true;
        }

        paymentRecordRepository.save(record);

        return PaymentInquiryResult.builder()
                .gatewayResponse(response)
                .rewardApplied(rewardApplied || record.isRewardApplied())
                .rewardMessage(rewardMessage != null ? rewardMessage : record.getRewardMessage())
                .build();
    }

    @Transactional
    public GatewayApiResponse<GatewayPaymentCloseData> close(PaymentCloseRequest request) {
        User user = getAuthenticatedUser();
        String accessToken = getDecryptedAccessToken(user);

        PaymentRecord record = paymentRecordRepository.findByPaymentId(request.getPaymentId().trim())
                .orElseThrow(() -> new RuntimeException("Pago no registrado en este backend"));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para cerrar este pago");
        }

        GatewayApiResponse<GatewayPaymentCloseData> response =
                paymentGatewayClient.closePayment(accessToken, request.getPaymentId().trim());

        if (response != null && response.isSuccess()) {
            record.setPaymentStatus("CLOSED");
            paymentRecordRepository.save(record);
        }

        return response;
    }

    private void validateCreateRequest(PaymentCreateRequest request) {
        String amountValue = request.getOrderAmount().getValue();
        try {
            double value = Double.parseDouble(amountValue);
            if (value <= 0) {
                throw new RuntimeException("El monto debe ser mayor a 0");
            }
        } catch (NumberFormatException ex) {
            throw new RuntimeException("orderAmount.value debe ser numerico");
        }

        if (request.getPackageId() != null) {
            String normalized = normalizePackageId(request.getPackageId());
            boolean exists = ShopConstants.TF_PACKAGES.stream().anyMatch(p -> p.getId().equals(normalized));
            if (!exists) {
                throw new RuntimeException("packageId no valido");
            }
        }
    }

    private String normalizePackageId(String packageId) {
        if (packageId == null) {
            return null;
        }
        String normalized = packageId.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        final Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Token de usuario invalido");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private String getDecryptedAccessToken(User user) {
        String encryptedToken = user.getCurrentAccessToken();
        if (encryptedToken == null || encryptedToken.isBlank()) {
            throw new RuntimeException("El usuario no tiene token de Super API para pagos");
        }

        try {
            return encryptor.decrypt(encryptedToken);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo descifrar el token de Super API");
        }
    }
}

